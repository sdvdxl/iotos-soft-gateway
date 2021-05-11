#!/usr/bin/env bash

mvn clean deploy -DskipTests=true -DaltDeploymentRepository=hekr-maven::default::file:maven/repository/
