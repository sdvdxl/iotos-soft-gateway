package hekr.me.iotcloudgateway.gateway.device;

import lombok.Data;

/**
 * 两种设备
 */
@Data
public class Device {
  /** 终端设备id */
  private String terminalID;
  /** iot设备id */
  private String devId;
  /** iot设备产品pk */
  private String pk;
  /** iot设备名称 */
  private String devName;
  /** iot设备所属产品密钥 */
  private String productSecret;
}
