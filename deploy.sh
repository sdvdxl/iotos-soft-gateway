#!/usr/bin/env bash
# 使用 ./deploy install 安装到本地
# ./deploy deploy 生成 maven jar包
# ./deploy upload 生成 maven jar 包并 commit

VERSION="3.2.3-SNAPSHOT"
echo "版本号：$VERSION"

updateVersion(){
  mvn clean
  mvn versions:set -DnewVersion=$VERSION
  mvn versions:update-properties
  mvn versions:update-child-modules
}

act=$1

deploy() {
  updateVersion
  mvn deploy
}


install() {
  updateVersion
  mvn clean install -DskipTests=true
}

if [[ "deploy" == "$act" ]] ; then
  deploy
elif [[ "version" == "$act" ]] ; then
  updateVersion
elif [[ "install" == "$act" || "" == "$act" ]] ; then
  install
else
  echo 'deploy 或者 install'
fi
