#!/usr/bin/env bash
# 使用 ./deploy install 安装到本地
# ./deploy deploy 生成 maven jar包
# ./deploy upload 生成 maven jar 包并 commit

VERSION="3.0.2"
echo "版本号：$VERSION"

updateVersion(){
  mvn versions:set -DnewVersion=$VERSION versions:update-child-modules
}

act=$1

deploy() {
  updateVersion
  rm -rf maven
  mvn clean deploy -DskipTests=true -DaltDeploymentRepository=hekr-maven::default::file:maven/repository/

  echo "发布位置：maven/repository"
}

install() {
  updateVersion
  mvn clean install -DskipTests=true
}

upload(){
  git add -A
  git commit -m "update version to: $VERSION"
  git push
}

if [[ "deploy" = "$act" ]] ; then
  deploy
elif [[ "install" = "$act" ]] ; then
  install
elif [[ "upload" = "$act" ]] ; then
  upload
else
  echo 'deploy 或者 install'
fi
