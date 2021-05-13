package me.hekr.iotos.softgateway.network.mqtt.listener;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.mqtt.ConnectionContext;

/** @author iotos */
@Slf4j
public abstract class AbstractListenerAdapter<T> implements Listener<T> {

  @Override
  public void onConnect(ConnectionContext<T> context) {
    log.info("建立连接，clientid:{}, address:{}", context.getClientId(), context.getAddress());
  }

  @Override
  public void onDisconnect(ConnectionContext<T> context) {
    log.info("断开连接，clientId:{}", context.getClientId());
  }

  @Override
  public void onMessage(ConnectionContext<T> context, String topic, MqttQoS qos, T payload) {
    log.info(
        "收到消息，clientId:{},topic:{},qos:{},message:{}", context.getClientId(), topic, qos, payload);
    context.publish(topic + "/resp", payload);
  }

  @Override
  public void onClose(ConnectionContext<T> context) {
    log.info("关闭连接，clientId:{}", context.getClientId());
  }
}
