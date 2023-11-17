package me.hekr.iotos.softgateway.core.listener;

/**
 * 设备配置监听器
 *
 * @author iotos
 * @version $Id: $Id
 */
public interface DeviceRemoteConfigListener {
  /**
   * DeviceRemoteConfig 第一次刷新前监听 ，用户一般用不到（初始化设备映射之前）
   */
  void firstBefore();

  /**
   * DeviceRemoteConfig 第一次刷新后监听 ，一般用于初始化工作（初始化映射设备之后）
   */
  void firstAfter();

  /**
   * DeviceRemoteConfig 刷新前监听 用户一般用不到
   */
  void before();

  /**
   * DeviceRemoteConfig 刷新后监听
   */
  void after();
}
