package me.hekr.iotos.softgateway.core.network.mqtt;

/**
 * mqtt 连接成功后监听器
 *
 * @author du
 */
public interface MqttConnectedListener {

  /** 连接成功回调 */
  void onConnected();

  /** 重连成功回调 */
  void onReConnected();
}
