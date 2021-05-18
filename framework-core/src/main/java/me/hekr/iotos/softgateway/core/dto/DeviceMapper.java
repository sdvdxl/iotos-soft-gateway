package me.hekr.iotos.softgateway.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.Transient;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig.Props;

/** @author iotos */
public interface DeviceMapper {

  /**
   * 获取映射关系的属性
   *
   * @return 自定义属性
   */
  @Transient
  @JsonIgnore
  Props getProps();
}
