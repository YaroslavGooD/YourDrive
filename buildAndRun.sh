#!/bin/bash
docker-compose down
docker-compose build
mvn clean install -f backend/pom.xml
docker-compose up
