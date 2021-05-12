#!/usr/bin/env bash
VERSION="2.0.0"
mvn versions:set -Dversion=$VERSION
mvn versions:update-child-modules

mvn  clean install deploy -DskipTests=true -DaltDeploymentRepository=hekr-maven::default::file:maven/repository/
