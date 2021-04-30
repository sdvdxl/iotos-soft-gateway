package me.hekr.iotos.softgateway.core.network.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** @author iotos */
@Slf4j
@Service
public class CoreMqttDisConnectListener implements MqttDisConnectListener {

  @Override
  public void onConnectFailed() {
    log.error("监听到连接失败");
  }

  @Override
  public void onDisconnect() {
    log.error("监听到连接断开");
  }
}
