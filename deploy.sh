#!/usr/bin/env bash
# 使用 ./deploy install 安装到本地
# ./deploy deploy 生成 maven jar包
# ./deploy upload 生成 maven jar 包并 commit
set -e

VERSION="3.5.1-SNAPSHOT"
echo "版本号：$VERSION"

updateVersion() {
  ./mvnw clean
  ./mvnw versions:set -DnewVersion=$VERSION
  ./mvnw versions:commit
#  ./mvnw versions:update-properties
  ./mvnw versions:update-child-modules
}

act=$1

remote() {
  updateVersion
  ./mvnw deploy -P deploy
}

install() {
  updateVersion
  ./mvnw clean install -P remote -DskipTests=true
}

if [[ "remote" == "$act" ]]; then
  remote
elif [[ "version" == "$act" ]]; then
  updateVersion
elif [[ "install" == "$act" || "" == "$act" ]]; then
  install
else
  echo 'remote(发布到maven仓库) ，version(更新版本) 或者 install(发布到本地仓库)'
fi
