#! /bin/sh

#$1 release version
#$2 dev version

if [ -z "$1" ] || [ -z "$2" ]; then
    echo "var is blank";
else
    mvn --batch-mode \
        -Dtag=dimmer-$1 \
        -Dproject.rel.com.github.cloudyrock.dimmer:dimmer-core2=$1 \
        -Dproject.dev.com.github.cloudyrock.dimmer:dimmer-core2=$2-SNAPSHOT \
        release:prepare;
    mvn release:perform
fi
