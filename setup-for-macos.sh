#!/bin/bash

function brew-install-version {
    export HOMEBREW_NO_INSTALL_CLEANUP=1
    export HOMEBREW_NO_AUTO_UPDATE=1
    
    formula_name=$1
    formula_version=$2

    git -C "$(brew --repo homebrew/core)" fetch --unshallow || echo "Homebrew repo already unshallowed"

    commit=$(brew log --max-count=50 --oneline "$formula_name" | grep "$formula_version" | head -n1 | cut -d ' ' -f1)

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
    brew unlink python@3.8
    brew uninstall ruby@2.6
    brew uninstall sqlite
    brew uninstall llvm
    brew uninstall node@12
    brew uninstall php
    brew uninstall postgresql
}

source lib/VERSIONS
command -v vipsthumbnail && vipsthumbnail --vips-version | grep -q "${VIPS_VERSION}" || {
    brew-install-version vips "${VIPS_VERSION}" || {
        echo >&2 "Vips not installed."
        exit 1
    }
}
