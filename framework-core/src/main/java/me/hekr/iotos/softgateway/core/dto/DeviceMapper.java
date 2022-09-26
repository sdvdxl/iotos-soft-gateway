package me.hekr.iotos.softgateway.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.Transient;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig.Props;

/**
 * @author iotos
 */
public interface DeviceMapper {

  /**
   * 获取映射关系的属性
   *
   * @return 自定义属性
   */
  @Transient
  @JsonIgnore
  Props getProps();

  /**
   * 获取iotos pk
   *
   * @return pk 或者null（设备无映射）
   */
  default String getIotPK() {
    return DeviceRemoteConfig.getByDeviceMapper(this).map(DeviceRemoteConfig::getPk).orElse(null);
  }

  /**
   * 获取iotos devId
   *
   * @return devId 或者null（设备无映射）
   */
  default String getIotDevId() {
    return DeviceRemoteConfig.getByDeviceMapper(this)
        .map(DeviceRemoteConfig::getDevId)
        .orElse(null);
  }
}
