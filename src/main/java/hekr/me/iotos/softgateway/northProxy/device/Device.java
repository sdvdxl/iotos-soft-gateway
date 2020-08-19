package hekr.me.iotos.softgateway.northProxy.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** 设备 */
@Data
public class Device {
  /** iot设备id */
  private String devId;
  //  /** iot设备产品pk */
  //  private String pk;
  /** iot设备名称 */
  private String devName;
  //  /** iot设备所属产品密钥 */
  //  private String productSecret;

  /** 防区编号 */
  private int areaNum;
}
