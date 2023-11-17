package me.hekr.iotos.softgateway.core.network.mqtt;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.listener.MqttConnectedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>CoreMqttConnectedListenerImpl class.</p>
 *
 * @version $Id: $Id
 */
@Service
@Slf4j
public class CoreMqttConnectedListenerImpl implements MqttConnectedListener {
  @Autowired private KlinkService klinkService;

  /** {@inheritDoc} */
  @Override
  public void onConnected() {
    log.info("监听到连接成功，触发 getConfig");
    klinkService.getConfig();
  }

  /** {@inheritDoc} */
  @Override
  public void onReConnected() {
    log.info("监听到重连成功");
    klinkService.getConfig();
  }
}
