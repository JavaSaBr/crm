#!/usr/bin/env sh

export PGPASSWORD=root

psql -U postgres  -tc "SELECT 1 FROM pg_database WHERE datname = 'jcrm'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE jcrm"
psql -d jcrm -U postgres -f jcrm.sql

