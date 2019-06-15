#! /bin/sh


#TODO get version from pom, instead of parameter. and work out the type of release(breaking change, minor, hotfix) from branch
#$1 release version
#$2 dev version
if [ -z "$1" ] || [ -z "$2" ]; then
    echo "var is blank";
else
    mvn --batch-mode \
        -Dtag=dimmer-$1 \
        -Dproject.rel.com.github.cloudyrock.dimmer:dimmer-parent=$1 \
        -Dproject.dev.com.github.cloudyrock.dimmer:dimmer-parent=$2-SNAPSHOT \
        -Dproject.rel.com.github.cloudyrock.dimmer:dimmer-core=$1 \
        -Dproject.dev.com.github.cloudyrock.dimmer:dimmer-core=$2-SNAPSHOT \
        release:prepare
    if [ $? -eq 0 ]; then
        mvn release:perform
    fi
fi
