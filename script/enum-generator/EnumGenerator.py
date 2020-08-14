import os
import glob
import re
import argparse
import wget
import tarfile
from bs4 import BeautifulSoup
from string import Template
from itertools import takewhile

DEFAULT_VIPS_VERSION = '8.9.0'
JAVA_ENUM_TEMPLATE = "template/Enum.java"
C_ENUM_TEST_TEMPLATE = "template/VipsEnumTest.c"


def traverse(root):
    # root is the path to html doc
    files = [os.path.join(root, f) for f in os.listdir(root)]
    sources = []
    for filename in files:
        if (os.path.isdir(filename)):
            traverse(filename)
        elif (os.path.isfile(filename)):
            source = read_html_source(filename)
            if source is not None:
                sources.append(source)
    return sources


def read_html_source(filename):
    ext = os.path.splitext(filename)[1]
    if (ext != '.html'):
        return None
    with open(filename, 'r', encoding='utf-8') as infile:
        return infile.read()


def build_dict(sources):
    dictionary = {}
    for source in sources:
        soup = BeautifulSoup(source, features="html.parser")
        refsect2 = soup.find_all('div', 'refsect2')
        for ref in refsect2:
            name = ref.find('a')['name']
            header = ref.find('h3').string
            if header is not None:
                dictionary[name] = {'name': header, 'data': {}}
                # get some extra information for the function
                program = ref.find('pre', 'programlisting')
                if program is not None:
                    dictionary[name]['data']['program'] = program
                # get optional arguments information
                optional = ref.find('ul', 'itemizedlist')
                if optional is not None:
                    dictionary[name]['data']['optional'] = optional
        refsect3 = soup.find_all('div', 'refsect3')
        for ref in refsect3:
            components = ref.find('a')['name'].split('.')
            if len(components) <= 1:
                break
            name = components[0]
            subname = components[1]
            if name in dictionary:
                tbody = ref.find_all('tr')
                dictionary[name]['data'][subname] = tbody
    return dictionary


def traverse_dictionary(dictionary, enum_output_dir, test_output_dir, license_comment):
    tests = []
    for key, item in sorted(dictionary.items()):
        if 'enum ' in item['name'] and 'members' in item['data']:
            compute_enum(item, tests, enum_output_dir, license_comment)

    tests = ''.join(tests)
    # generate enum tests
    with open(C_ENUM_TEST_TEMPLATE, 'r', encoding='utf-8') as infile:
        tpl = infile.read()
        with open(f'{test_output_dir}/{os.path.basename(C_ENUM_TEST_TEMPLATE)}', 'w', encoding='utf-8') as outfile:
            src = Template(tpl)
            src = src.substitute({'license': license_comment, 'tests': tests})
            outfile.write(src)

# Enums


enum_overwrites = {
    'VIPS_OPERATION_NONE': 0,
    'VIPS_OPERATION_SEQUENTIAL': 1,
    'VIPS_OPERATION_SEQUENTIAL_UNBUFFERED': 2,
    'VIPS_OPERATION_NOCACHE': 4,
    'VIPS_OPERATION_DEPRECATED': 8,
    'VIPS_FOREIGN_NONE': 0,
    'VIPS_FOREIGN_PARTIAL': 1,
    'VIPS_FOREIGN_BIGENDIAN': 2,
    'VIPS_FOREIGN_SEQUENTIAL': 4,
    'VIPS_FOREIGN_ALL': 7,

    'VIPS_ARGUMENT_NONE': 0,
    'VIPS_ARGUMENT_REQUIRED': 1,
    'VIPS_ARGUMENT_CONSTRUCT': 2,
    'VIPS_ARGUMENT_SET_ONCE': 4,
    'VIPS_ARGUMENT_SET_ALWAYS': 8,
    'VIPS_ARGUMENT_INPUT': 16,
    'VIPS_ARGUMENT_OUTPUT': 32,
    'VIPS_ARGUMENT_DEPRECATED': 64,
    'VIPS_ARGUMENT_MODIFY': 128,

    'VIPS_DEMAND_STYLE_ERROR': -1,

    'VIPS_IMAGE_ERROR': -1,

    'VIPS_INTERPRETATION_ERROR': -1,
    'VIPS_INTERPRETATION_MULTIBAND': 0,
    'VIPS_INTERPRETATION_B_W': 1,
    'VIPS_INTERPRETATION_HISTOGRAM': 10,
    'VIPS_INTERPRETATION_XYZ': 12,
    'VIPS_INTERPRETATION_LAB': 13,
    'VIPS_INTERPRETATION_CMYK': 15,
    'VIPS_INTERPRETATION_LABQ': 16,
    'VIPS_INTERPRETATION_RGB': 17,
    'VIPS_INTERPRETATION_CMC': 18,
    'VIPS_INTERPRETATION_LCH': 19,
    'VIPS_INTERPRETATION_LABS': 21,
    'VIPS_INTERPRETATION_sRGB': 22,
    'VIPS_INTERPRETATION_YXY': 23,
    'VIPS_INTERPRETATION_FOURIER': 24,
    'VIPS_INTERPRETATION_RGB16': 25,
    'VIPS_INTERPRETATION_GREY16': 26,
    'VIPS_INTERPRETATION_MATRIX': 27,
    'VIPS_INTERPRETATION_scRGB': 28,
    'VIPS_INTERPRETATION_HSV': 29,
    'VIPS_INTERPRETATION_LAST': 30,

    'VIPS_FORMAT_NOTSET': -1,

    'VIPS_CODING_ERROR': -1,
    'VIPS_CODING_NONE': 0,
    'VIPS_CODING_LABQ': 2,
    'VIPS_CODING_RAD': 6,
    'VIPS_CODING_LAST': 7,

    'VIPS_TOKEN_LEFT': 1,

    'VIPS_FOREIGN_TIFF_PREDICTOR_NONE': 1,

    'VIPS_FOREIGN_PNG_FILTER_NONE': int("0x08", 16),
    'VIPS_FOREIGN_PNG_FILTER_SUB': int("0x10", 16),
    'VIPS_FOREIGN_PNG_FILTER_UP': int("0x20", 16),
    'VIPS_FOREIGN_PNG_FILTER_AVG': int("0x40", 16),
    'VIPS_FOREIGN_PNG_FILTER_PAETH': int("0x80", 16),
    'VIPS_FOREIGN_PNG_FILTER_ALL': int("0xF8", 16),

    'VIPS_FOREIGN_HEIF_COMPRESSION_HEVC': 1,
}


def compute_enum(item, tests, enum_output_dir, license_comment):
    members = []
    # remove 'enum' prefix
    name = item['name'].split(' ')[1]
    tbody = item['data']['members']
    for tr in tbody:
        member_name = tr.find('td', 'enum_member_name').p.contents[1]
        member_desc = tr.find('td', 'enum_member_description')
        description = None
        if member_desc and member_desc.p:
            description = member_desc.p.string
        members.append({'name': member_name, 'description': description})
    with open(JAVA_ENUM_TEMPLATE, 'r', encoding='utf-8') as infile:
        tpl = infile.read()
        sep = ',\n'
        cpt = 0
        values = []
        tests.append(f'    // {name}\n')
        for member in members:
            cname = member['name']
            value = to_pascal_case(cname)
            description = member['description']
            index = len(lcp(value, name))
            fcpt = cpt
            if cname in enum_overwrites:
                fcpt = enum_overwrites[cname]
                cpt = fcpt
            value = value[index:]
            tests.append(
                f'    assertEqualsNativeEnumValue(env, {cname}, "com/criteo/vips/enums/{name}", "{value}");\n')
            value = f'    {value}({fcpt})'
            cpt += 1
            if description is not None:
                value = f'    // {description}\n{value}'
            values.append(value)
        # generate enum class file
        with open(f'{enum_output_dir}/{name}.java', 'w', encoding='utf-8') as outfile:
            src = Template(tpl)
            values = sep.join(values)
            src = src.substitute(
                {'license': license_comment, 'name': name, 'values': values})
            outfile.write(src)

# Utils


def clean_html(html):
    regex = re.compile('<.*?>')
    return re.sub(regex, '', html)


def lcp(*s):
    return ''.join(a for a, b in takewhile(lambda x: x[0] == x[1], zip(min(s), max(s))))


def to_pascal_case(snakeCase):
    return snakeCase.title().replace('_', '')


def to_snake_case(pascalCase):
    return '_'.join(re.findall('[A-Z][^A-Z]*', pascalCase)).upper()


def main():
    parser = argparse.ArgumentParser(
        description='Generate Java enumerations from Vips documentation.')
    parser.add_argument('version', metavar='x.y.z', type=str, nargs='?', default=DEFAULT_VIPS_VERSION,
                        help='Vips version')

    args = parser.parse_args()
    version = args.version

    if not os.path.isfile('EnumGenerator.py'):
        raise Exception(
            "Script must run from the script/enum-generator directory")

    documentation = os.path.join(os.getcwd(), f'vips-{version}/doc/html')
    enum_output_dir = os.path.join(
        os.getcwd(), '../../src/main/java/com/criteo/vips/enums')
    test_output_dir = os.path.join(os.getcwd(), '../../src/test/c')

    for java_file in glob.glob(f'{enum_output_dir}/*.java'):
        os.remove(java_file)
    for c_file in glob.glob(f'{test_output_dir}/*.{{c,h}}'):
        os.remove(c_file)

    if not os.path.isdir(documentation):
        url = f'https://github.com/libvips/libvips/releases/download/v{version}/vips-{version}.tar.gz'
        tarball = wget.download(url)
        with tarfile.open(tarball) as tf:
            tf.extractall()

    with open(os.path.join(os.getcwd(), 'LICENSE'), 'r', encoding='utf-8') as infile:
        license_comment = infile.read()

    if not os.path.exists(enum_output_dir):
        os.makedirs(enum_output_dir)
    if not os.path.exists(test_output_dir):
        os.makedirs(test_output_dir)

    sources = traverse(documentation)
    dictionary = build_dict(sources)
    traverse_dictionary(dictionary, enum_output_dir,
                        test_output_dir, license_comment)


if __name__ == '__main__':
    main()
