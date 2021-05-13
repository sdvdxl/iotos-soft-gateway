package me.hekr.iotos.softgateway.network.mqtt.listener;

import io.netty.handler.codec.mqtt.MqttQoS;
import me.hekr.iotos.softgateway.network.mqtt.ConnectionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听器
 *
 * @author iotos
 */
public interface Listener<T> {
  Logger log = LoggerFactory.getLogger(Listener.class);
  /**
   * 建立连接
   *
   * @param context
   */
  void onConnect(ConnectionContext<T> context);

  /**
   * 断开连接
   *
   * @param context
   */
  void onDisconnect(ConnectionContext<T> context);

  /**
   * 收到消息
   *
   * @param context
   * @param topic
   * @param qos
   * @param payload
   */
  void onMessage(ConnectionContext<T> context, String topic, MqttQoS qos, T payload);

  /**
   * 连接关闭
   *
   * @param context
   */
  void onClose(ConnectionContext<T> context);

  /**
   * 认证
   *
   * @param context
   * @return
   */
  boolean auth(ConnectionContext<T> context);
}
