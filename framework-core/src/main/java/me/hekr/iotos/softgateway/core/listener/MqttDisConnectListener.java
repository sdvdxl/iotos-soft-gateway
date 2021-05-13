package me.hekr.iotos.softgateway.core.listener;

/**
 * mqtt 连接成功后监听器
 *
 * <p>如果需要监听，实现一个或者多个 bean
 *
 * @author du
 */
public interface MqttDisConnectListener {

  /** 连接失败 */
  void onConnectFailed();
  /** 连接断开 */
  void onDisconnect();
}
