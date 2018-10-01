#!/bin/bash

echo "--------------------------------------------------------------------Branch: $TRAVIS_BRANCH"
echo "--------------------------------------------------------------------Pull request key: $TRAVIS_PULL_REQUEST"
echo "--------------------------------------------------------------------Pull request branch: $TRAVIS_PULL_REQUEST_BRANCH"
echo "--------------------------------------------------------------------Encrypted variables: $TRAVIS_SECURE_ENV_VARS"

if [[ -z $TRAVIS_PULL_REQUEST_BRANCH ]]
then
    echo "Verifying and running sonar for  master branch"
    mvn clean verify sonar:sonar -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN
elif [[ $TRAVIS_SECURE_ENV_VARS = true ]]
then
    echo "Verifying and running sonar for  Pull request"
    mvn clean verify sonar:sonar -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN -Dsonar.pullrequest.base=master -Dsonar.pullrequest.key=$TRAVIS_PULL_REQUEST -Dsonar.pullrequest.branch=$TRAVIS_PULL_REQUEST_BRANCH
else
    echo "Verifying(NO SONAR) Pull request"
    mvn clean verify
fi