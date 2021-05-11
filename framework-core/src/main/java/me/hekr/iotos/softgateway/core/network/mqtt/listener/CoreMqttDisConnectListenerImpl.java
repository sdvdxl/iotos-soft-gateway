package me.hekr.iotos.softgateway.core.network.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * mqtt 连接断开监听器，如果需要监听，实现一个或者多个 bean
 *
 * @author iotos
 */
@Slf4j
@Service
public class CoreMqttDisConnectListenerImpl implements MqttDisConnectListener {

  @Override
  public void onConnectFailed() {
    log.error("监听到连接失败");
  }

  @Override
  public void onDisconnect() {
    log.error("监听到连接断开");
  }
}
