FROM centos:7.2.1511

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

RUN yum install ${YUM_OPTIONS} \
    centos-release-scl \
    https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm

# Install system dependencies.
RUN yum install ${YUM_OPTIONS} \
    wget \
    git \
    cmake3 \
    rh-maven35 \
    meson \
    ninja-build \
    patch \
    clang \
    devtoolset-7-toolchain \
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
    java-1.8.0-openjdk

RUN ln -s /usr/bin/cmake3 /usr/bin/cmake

# Switch to a non-root user.
RUN groupadd -g $GID -o $UNAME
RUN useradd -l -m -u $UID -g $GID -o -s /bin/bash -d /home/$UNAME $UNAME

RUN echo 'for scl in /opt/rh/*/enable; do source $scl; done' >> /home/$UNAME/.bashrc

USER $UNAME

CMD bash -l -ex build.sh --with-linux --without-w64 --without-macos
