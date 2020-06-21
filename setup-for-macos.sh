#!/bin/bash

function brew-install-version {
    formula_name=$1
    formula_version=$2

    git -C "$(brew --repo homebrew/core)" fetch --unshallow || echo "Homebrew repo already unshallowed"

    commit=$(brew log --max-count=50 --oneline $formula_name | grep $formula_version | head -n1 | cut -d ' ' -f1)
    formula=$(brew log --max-count=50 --oneline $formula_name | grep $formula_version | head -n1 | cut -d ':' -f1 | cut -d ' ' -f2)

    [ -z "${commit}" -o -z "${formula}" ] && {
        echo >&2 "No version matching '$formula_version' for '$formula_name'"
        return 1
    }

    [ ! -e /usr/local/bin/$formula ] || brew uninstall --force $formula_name
    brew install --build-from-source https://raw.githubusercontent.com/Homebrew/homebrew-core/$commit/Formula/$formula.rb

    echo "$formula_name $formula_version installed."
}

command -v brew || {
    echo >&2 "Homebrew missing."
    exit 1
}

command -v java || {
    brew tap homebrew/cask-versions
    brew cask install homebrew/cask-versions/adoptopenjdk8
}

command -v cmake || {
    brew install cmake
}

command -v pkg-config || {
    brew install pkgconfig
}

command -v vipsthumbnail || {
    source ./lib/variables.sh
    brew-install-version vips ${VIPS_VERSION} || {
        echo >&2 "Vips not installed."
        exit 1
    }
}
