#!/bin/bash
docker-compose down
mvn clean install -f backend/pom.xml
docker-compose build
docker-compose up
