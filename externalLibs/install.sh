#!/usr/bin/env bash

mvn install:install-file -Dfile=./rlib-5.1.0.jar -Dsources=./rlib-sources-5.1.0.jar -DgroupId=com.ss -DartifactId=rlib -Dversion=5.1.0 -Dpackaging=jar