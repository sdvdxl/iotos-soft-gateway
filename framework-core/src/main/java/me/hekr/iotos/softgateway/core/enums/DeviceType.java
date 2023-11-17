package me.hekr.iotos.softgateway.core.enums;

/**
 * <p>DeviceType class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
public enum DeviceType {
  /**
   * 普通独立设备
   *
   * <p>直接连接云端，下面不能挂载其他设备
   */
  GENERAL,

  /**
   * 交换机、中继设备
   *
   * <p>不能直接连接到云端（针对mqtt和tcp方式说的）
   *
   * <p>挂载到网关上，下面可以挂载其他中继设备或者子设备
   */
  SWITCH,

  /**
   * 网关设备
   *
   * <p>直接连接云端，下面可以挂载
   */
  GATEWAY,

  /**
   * 终端节点
   *
   * <p>不能直接连接到云端（针对mqtt和tcp方式说的）
   *
   * <p>只能当做叶子节点挂载到网关或者中继设备上
   */
  TERMINAL;

  /**
   * 是否是直连类型（独立设备，网关设备）
   *
   * @param deviceType 设备类型
   * @return 是 true，否则 false
   */
  public static boolean isDirectConnectType(DeviceType deviceType) {
    return deviceType == GENERAL || deviceType == GATEWAY;
  }
}
