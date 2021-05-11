package me.hekr.iotos.softgateway.core.subsystem;

import me.hekr.iotos.softgateway.core.config.DeviceMapper;
import me.hekr.iotos.softgateway.core.klink.ModelData;

/**
 * 控制命令
 *
 * <p>实现类要加 @Service("XXSubsystemCommandService")，其中 xx 为 IoTOS 平台物模型 命令
 *
 * @author iotos
 */
public interface SubsystemCommandService {

  /**
   * 处理下发命令
   *
   * @param deviceMapper 设备信息
   * @param data 物模型命令和参数
   */
  void handle(DeviceMapper deviceMapper, ModelData data);
}
