#!/usr/bin/env bash
VERSION="3.0.0-SNAPSHOT"
echo "版本号：$VERSION"
mvn versions:set -DnewVersion=$VERSION
mvn versions:update-child-modules

mvn  clean install -DskipTests=true

rm -rf maven
mvn deploy -DskipTests=true -DaltDeploymentRepository=hekr-maven::default::file:maven/repository/

echo "发布位置：maven/repository"
