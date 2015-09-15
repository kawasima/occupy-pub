#!/bin/bash

sudo -u postgres createuser -a -d cert
sudo -u postgres psql -c "ALTER USER cert WITH PASSWORD 'cert'"
sudo -u postgres createdb -E UTF-8 -T template0 -O cert occupy_pub

