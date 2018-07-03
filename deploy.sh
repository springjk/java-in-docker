#!/bin/bash

docker-compose exec maven mvn clean install -Dmaven.test.skip=true


set -a
source .env
docker cp $CODE_PATH/target/*.war "$(docker-compose ps -q tomcat)":/usr/local/tomcat/webapps/ROOT.war

