package me.hekr.iotos.softgateway.core.listener;

/**
 * 设备配置监听器
 *
 * @author iotos
 */
public interface DeviceRemoteConfigListener {
  /** DeviceRemoteConfig 起一次刷新前监听 ，一般用于初始化工作 */
  void firstBefore();

  /** DeviceRemoteConfig 第一次刷新后监听 ，一般用于初始化工作 */
  void firstAfter();
  /** DeviceRemoteConfig 刷新前监听 */
  void before();

  /** DeviceRemoteConfig 刷新后监听 */
  void after();
}
