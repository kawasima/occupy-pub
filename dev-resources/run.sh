#!/bin/bash


source /etc/apache2/envvars

[ ! -d ${APAHE_RUN_DIR:-/var/run/apache2} ] && mkdir -p ${APACHE_RUN_DIR:-/var/run/apache2}
[ ! -d ${APAHE_LOCK_DIR:-/var/lock/apache2} ] && mkdir -p ${APACHE_LOCK_DIR:-/var/lock/apache2}

tailf /var/log/apache2/* &
apache2 -D FOREGROUND
