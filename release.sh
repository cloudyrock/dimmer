#! /bin/sh

mvn --batch-mode \
    -Dtag=dimmer-1.4.7 \
    -Dproject.rel.com.github.cloudyrock.dimmer:dimmer-core2=1.4.7 \
    -Dproject.dev.com.github.cloudyrock.dimmer:dimmer-core2=1.4.8-SNAPSHOT \
    release:prepare
