#!/bin/bash
cd $2

kill `ps -eo pid,args --cols=10000 | awk '/-jar jcrm/ && $1 != PROCINFO["pid"] { print $1 }'`

rm jcrm.out
$1/bin/java -Ddb.upgrading.enabled=true -jar jcrm-full-web.jar > jcrm.out &