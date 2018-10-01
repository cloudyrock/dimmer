#!/bin/bash



echo "\n\n\n\n---------------------------------------------------------------------------------------------------------------------Branch: $TRAVIS_BRANCH\n\n\n\n"
echo "\n\n\n\n---------------------------------------------------------------------------------------------------------------------Encripted variables: $TRAVIS_SECURE_ENV_VARS\n\n\n\n"


if [[ $TRAVIS_BRANCH == 'master' ]]
then
    mvn clean verify sonar:sonar -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN
else
    mvn clean verify sonar:sonar -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=b3ad74729ae5d41a7248e50e686d44b755f3a092 -Dsonar.pullrequest.base=master -Dsonar.pullrequest.key=$TRAVIS_PULL_REQUEST -Dsonar.pullrequest.branch=$TRAVIS_PULL_REQUEST_BRANCH
fi

