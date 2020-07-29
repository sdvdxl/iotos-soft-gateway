package hekr.me.iotos.softgateway.northProxy.device;

import lombok.Data;

/** 设备 */
@Data
public class Device {
  /** iot设备id */
  private String devId;
  /** iot设备产品pk */
  private String pk;
  /** iot设备名称 */
  private String devName;
  /** iot设备所属产品密钥 */
  private String productSecret;

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
