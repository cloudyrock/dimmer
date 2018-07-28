#! /bin/sh

mvn --batch-mode \
    -Dtag=dimmer-1.4.5 \
    -Dproject.rel.com.github.cloudyrock.dimmer:test=1.4.5 \
    -Dproject.dev.com.github.cloudyrock.dimmer:test=1.4.6 \
    release:prepare
