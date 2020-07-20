package hekr.me.iotos.softgateway.northProxy.device;

import lombok.Data;

/** 设备 */
@Data
public class Device {
  /** 通道id */
  private String channelID;
  /** 通道名称 */
  private String channelName;
  /** iot设备id */
  private String devId;
  /** iot设备产品pk */
  private String pk;
  /** iot设备名称 */
  private String devName;
  /** iot设备所属产品密钥 */
  private String productSecret;
}
