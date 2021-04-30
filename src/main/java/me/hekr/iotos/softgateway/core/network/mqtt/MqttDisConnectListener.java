package me.hekr.iotos.softgateway.core.network.mqtt;

/**
 * mqtt 连接成功后监听器
 *
 * @author du
 */
public interface MqttDisConnectListener {

  /** 连接失败 */
  void onConnectFailed();
  /** 连接断开 */
  void onDisconnect();
}
