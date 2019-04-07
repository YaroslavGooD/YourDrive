#!/bin/bash
mvn clean install -f backend/pom.xml
docker-compose up -d
