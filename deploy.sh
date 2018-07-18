#!/bin/bash

docker-compose exec maven mvn clean package -T 4C -Dmaven.test.skip=true  -Dmaven.compile.fork=true


set -a
source .env
docker cp $CODE_PATH/target/*.war "$(docker-compose ps -q tomcat)":/usr/local/tomcat/webapps/ROOT.war

