package me.hekr.iotos.softgateway.core.network.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.klink.KlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author du */
@Service
@Slf4j
public class CoreMqttConnectedListenerImpl implements MqttConnectedListener {
  @Autowired private KlinkService klinkService;

  @Override
  public void onConnected() {
    log.info("监听到连接成功，触发 getConfig");
    klinkService.getConfig();
  }

  @Override
  public void onReConnected() {
    log.info("监听到重连成功");
    klinkService.getConfig();
  }
}
