#!/usr/bin/env bash

mvn clean install deploy -DskipTests=true -DaltDeploymentRepository=hekr-maven::default::file:maven/repository/
