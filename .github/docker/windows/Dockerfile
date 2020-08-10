FROM fedora:32

# Set default build arguments.
ARG NODE_VERSION=10.x

# Set default user (overriden in the command line with Jenkins' actual user).
ARG UNAME=jenkins
ARG UID=1000
ARG GID=1000

# Set default environment variables.
ENV JAVA_HOME=/usr/lib/jvm/java-openjdk
ENV PATH="${OSX_CROSS_HOME}/bin:${PATH}"
ENV YUM_OPTIONS="-y --setopt=skip_missing_names_on_install=False"

# Install system dependencies.
RUN yum install ${YUM_OPTIONS} \
    wget \
    git \
    make \
    cmake \
    maven \
    meson \
    ninja-build \
    patch \
    gcc-c++ \
    clang \
    nasm \
    autoconf \
    automake \
    libtool \
    diffutils \
    openssl-devel \
    expat-devel \
    zlib-devel \
    libxml2-devel \
    xz xz-devel \
    mpfr-devel \
    gmp-devel \
    libmpc-devel \
    gtk-doc \
    gobject-introspection gobject-introspection-devel \
    glib2.x86_64 glib2-devel.x86_64 \
    java-1.8.0-openjdk \
    mingw-w64-tools \
    mingw64-gcc \
    mingw64-gcc-c++ \
    mingw64-glib2 \
    mingw64-win-iconv \
    mingw64-expat

# Link the system version of libmpfr, which is more recent than expected, but works fine.
RUN ln -s /lib64/libmpfr.so.6 /lib64/libmpfr.so.4

# Switch to a non-root user.
RUN groupadd -g $GID -o $UNAME
RUN useradd -l -m -u $UID -g $GID -o -s /bin/bash -d /home/$UNAME $UNAME
USER $UNAME

CMD bash -ex build.sh --with-w64 --without-linux --without-macos --skip-test
