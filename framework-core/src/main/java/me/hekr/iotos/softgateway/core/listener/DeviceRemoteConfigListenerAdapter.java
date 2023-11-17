package me.hekr.iotos.softgateway.core.listener;

import lombok.extern.slf4j.Slf4j;

/** @author iotos */
/**
 * <p>DeviceRemoteConfigListenerAdapter class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Slf4j
public class DeviceRemoteConfigListenerAdapter implements DeviceRemoteConfigListener {

  /** {@inheritDoc} */
  @Override
  public void firstBefore() {
    log.info("第一次调用DeviceRemoteConfig之前");
  }

  /** {@inheritDoc} */
  @Override
  public void firstAfter() {
    log.info("第一次调用DeviceRemoteConfig之后");
  }

  /** {@inheritDoc} */
  @Override
  public void before() {
    log.info("调用DeviceRemoteConfig之前");
  }

  /** {@inheritDoc} */
  @Override
  public void after() {
    log.info("调用DeviceRemoteConfig之后");
  }
}
