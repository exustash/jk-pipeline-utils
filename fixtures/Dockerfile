FROM bitnami/minideb:stretch

LABEL maintainer="Exuper O.<e.okouya@checkmate.com>"

# Add checkmate proxy in apt.conf.d
#COPY config/91apt-proxy /etc/apt/apt.conf.d/

# System Packages
RUN install_packages vim git curl tar zip unzip openssh-server wget dirmngr apt-utils rsync gosu build-essential software-properties-common &&\
    install_packages python3 python3-setuptools python-setuptools python3-dev python3-pip python-dev &&\
    ln -s /usr/bin/pip3 /usr/bin/pip &&\
    pip install --upgrade pip setuptools wheel
