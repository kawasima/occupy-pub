FROM ubuntu:trusty
MAINTAINER kawasima <kawasima1016@gmail.com>

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get -yq install \
    apache2 \
    openssl \
    postgresql-9.3 \
    libaprutil1 \
    libaprutil1-dbd-pgsql

ADD client-auth.conf /etc/apache2/sites-available/
RUN a2enmod ssl headers authz_dbd authn_dbd
RUN a2ensite client-auth

ADD certs/* /etc/ssl/certs/
ADD private/* /etc/ssl/private/

# Shellscript setup
ADD run.sh pgsql-setup.sh /
RUN chmod 755 /*.sh

# Database setup
RUN /pgsql-setup.sh

EXPOSE 443

CMD ["/run.sh"]

