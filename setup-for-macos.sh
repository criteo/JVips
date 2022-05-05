#!/bin/bash

function brew-install-version {
    export HOMEBREW_NO_INSTALL_CLEANUP=1
    export HOMEBREW_NO_AUTO_UPDATE=1
    
    formula_name=$1
    formula_version=$2

    git -C "$(brew --repo homebrew/core)" fetch --unshallow || echo "Homebrew repo already unshallowed"

    commit=$(brew log --max-count=50 --oneline "$formula_name" | grep "$formula_version" | head -n1 | cut -d ' ' -f1)
    # vips 8.12.2 broken repository fix. See: https://github.com/criteo/JVips/pull/127#issuecomment-1119602094
    commit="6a728cd675"

    [ -z "$commit" ] && {
        echo >&2 "No version matching '$formula_version' for '$formula_name'"
        return 1
    }

    git -C "$(brew --repo homebrew/core)" reset --hard "$commit"

    brew install "$formula_name" || {
        echo >&2 "No current formula for '$formula_name'"
        return 1
    }

    echo "$formula_name $formula_version installed."
}

command -v brew || {
    echo >&2 "Homebrew missing."
    exit 1
}

command -v brew update || {
    brew update
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

[ -z "$GITHUB_ACTIONS" ] || {
    rm /usr/local/bin/2to3
    brew unlink python@3.9
    brew uninstall --ignore-dependencies sqlite
    brew uninstall llvm
    brew uninstall --ignore-dependencies php
    brew uninstall postgresql
}

source lib/VERSIONS
command -v vipsthumbnail && vipsthumbnail --vips-version | grep -q "${VIPS_VERSION}" || {
    brew-install-version vips "${VIPS_VERSION}" || {
        echo >&2 "Vips not installed."
        exit 1
    }
}
