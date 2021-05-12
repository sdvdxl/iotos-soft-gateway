#!/usr/bin/env bash
VERSION="2.0.2-SNAPSHOT"
echo "版本号：$VERSION"
mvn versions:set -DnewVersion=$VERSION
mvn versions:update-child-modules

mvn  clean install -DskipTests=true
