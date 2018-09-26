#!/bin/bash

if [[ $TRAVIS_BRANCH == 'master' ]]
then
    mvn clean verify sonar:sonar -P noCoverage -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=b3ad74729ae5d41a7248e50e686d44b755f3a092
else
    mvn clean verify sonar:sonar -P noCoverage -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=b3ad74729ae5d41a7248e50e686d44b755f3a092 -Dsonar.pullrequest.base=master -Dsonar.pullrequest.key=$TRAVIS_PULL_REQUEST -Dsonar.pullrequest.branch=$TRAVIS_PULL_REQUEST_BRANCH
fi

