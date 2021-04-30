package me.hekr.iotos.softgateway.core.common.dto;

import lombok.Data;

/**
 * @author iotos
 * @date 2020/7/29
 */
@Data
public class DeviceDto {
  /** 设备Id */
  private String Id;
  /** 设备名称 */
  private String Name;
  /** 设备类别Id */
  private String DeviceTypeId;
  /** 区域Id */
  private String AreaId;
  /** 客户编号 */
  private String CustomerCode;
  /** 父设备Id */
  private String ParentId;
}
