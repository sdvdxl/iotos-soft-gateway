package me.hekr.iotos.softgateway.core.subsystem;

import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.klink.ModelData;

/**
 * 控制命令
 *
 * <p>如果抛出CloudSendException异常，则错误码是400，其他异常500.
 *
 * <p>不抛出异常视为成功，错误码为0
 *
 * <p>实现类要加 @Service("XX{#deviceType}@SubSystemCommandService")，其中 xx 为 IoTOS 平台物模型 命令
 *
 * <p>{#deviceType} 是对应的远程配置中的 deviceType 的值，注意只能使用英文，可以不填。
 *
 * @author iotos
 * @version $Id: $Id
 */
public interface SubsystemCommandService {

  /**
   * 处理下发命令
   *
   * @param deviceRemoteConfig 设备信息
   * @param data 物模型命令和参数
   */
  void handle(DeviceRemoteConfig deviceRemoteConfig, ModelData data);
}
