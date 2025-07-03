#!/bin/bash

echo "Installing SharedLib"
mvn clean install -f sharedlib/pom.xml -DskipTests

echo "Installing Game Catalog Service"
mvn clean install -f gamecatalog/pom.xml -DskipTests

echo "Building Game Catalog Service's docker image"
docker build -t filipem/gamecatalog-service:1.2.0 gamecatalog/.

echo "Installing Game Order Service"
mvn clean install -f gameorder/pom.xml -DskipTests

echo "Building Game Order Service's docker image"
docker build -t filipem/gameorder-service:1.2.0 gameorder/.

echo "Installing Keycloak Service"
mvn clean install -f keycloak-service/pom.xml -DskipTests

echo "Building Keycloak Service's docker image"
docker build -t filipem/keycloak-service:1.2.0 keycloak-service/.

echo "Installing Eureka Service"
mvn clean install -f eureka-server/pom.xml -DskipTests

echo "Building Eureka Service's docker image"
docker build -t filipem/eureka-service:1.1.0 gamecatalog/.

echo "Done"