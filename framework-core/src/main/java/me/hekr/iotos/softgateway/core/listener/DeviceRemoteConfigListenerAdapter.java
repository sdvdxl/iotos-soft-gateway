package me.hekr.iotos.softgateway.core.listener;

import lombok.extern.slf4j.Slf4j;

/** @author iotos */
@Slf4j
public class DeviceRemoteConfigListenerAdapter implements DeviceRemoteConfigListener {

  @Override
  public void firstBefore() {
    log.info("第一次调用DeviceRemoteConfig之前");
  }

  @Override
  public void firstAfter() {
    log.info("第一次调用DeviceRemoteConfig之后");
  }

  @Override
  public void before() {
    log.info("调用DeviceRemoteConfig之前");
  }

  @Override
  public void after() {
    log.info("调用DeviceRemoteConfig之后");
  }
}
