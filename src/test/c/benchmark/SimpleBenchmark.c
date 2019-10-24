/*
  Copyright (c) 2020 Criteo

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

#include <stdio.h>
#include <time.h>
#include <vips/vips.h>
#include <gobject/gobject.h>

int benchmark(void *buf, size_t len)
{
    VipsImage *image;
    VipsImage *x;

    void *new_buf;
    size_t new_len;

    if (vips_thumbnail_buffer(buf, len, &image, 512, "height", 512, NULL))
    {
        g_object_unref(image);
        return -1;
    }

    if (vips_crop(image, &x, 128, 128, 128, 128, NULL))
    {
        g_object_unref(image);
        return -1;
    }
    g_object_unref(image);
    image = x;

    if (vips_gravity(image, &x, VIPS_COMPASS_DIRECTION_CENTRE, 256, 256, NULL))
    {
        g_object_unref(image);
        return -1;
    }
    g_object_unref(image);
    image = x;

    if (vips_image_write_to_buffer(image, ".jpg", 
        &new_buf, &new_len, NULL)) {
        g_object_unref(image);
        return -1;
    }

    g_free(new_buf);
    g_object_unref(image);

    return 0;
}

int main(int argc, char **argv)
{
    const int N = 100;
    float start, stop;
    gchar *contents;
    gsize length;

    if (VIPS_INIT(argv[0]))
        vips_error_exit(NULL);

    /* Turn off the libvips cache for this benchmark, or some operations
     * may get reused.
     */
    vips_cache_set_max(0);

    if (!g_file_get_contents(argv[1], &contents, &length, NULL))
        vips_error_exit(NULL);

    start = (float) clock() / CLOCKS_PER_SEC;

    for (int i = 0; i < N; i++)
        if (benchmark(contents, length))
            vips_error_exit(NULL);

    stop = (float) clock() / CLOCKS_PER_SEC;

    double total = (stop - start) * 1000; // in ms
    double op_time = total / N;
    printf("SimpleBenchmark ran %d iterations in %.2lf ms (%.2lf ms/op)\n", N, total, op_time);

    g_free(contents);

    return 0;
}