#!/bin/bash



echo "---------------------------------------------------------------------------------------------------------------------Branch: $TRAVIS_BRANCH"
echo "---------------------------------------------------------------------------------------------------------------------Pull request key: $TRAVIS_PULL_REQUEST"
echo "---------------------------------------------------------------------------------------------------------------------Pull request branch: $TRAVIS_PULL_REQUEST_BRANCH"
echo "---------------------------------------------------------------------------------------------------------------------Encripted variables: $TRAVIS_SECURE_ENV_VARS"


if [[ $TRAVIS_BRANCH == 'master' ]]
then
    mvn clean verify sonar:sonar -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN
else
    mvn clean verify sonar:sonar -Dsonar.projectKey=com.github.cloudyrock.dimmer -Dsonar.organization=cloudyrock -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN -Dsonar.pullrequest.base=master -Dsonar.pullrequest.key=$TRAVIS_PULL_REQUEST -Dsonar.pullrequest.branch=$TRAVIS_PULL_REQUEST_BRANCH
fi

