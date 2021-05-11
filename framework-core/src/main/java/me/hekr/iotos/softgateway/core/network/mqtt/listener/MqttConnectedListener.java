package me.hekr.iotos.softgateway.core.network.mqtt.listener;

/**
 * mqtt 连接成功后监听器。
 *
 * <p>如果需要监听，如果需要监听，实现一个或者多个 bean
 *
 * @author du
 */
public interface MqttConnectedListener {

  /** 连接成功回调 */
  void onConnected();

  /** 重连成功回调 */
  void onReConnected();
}
