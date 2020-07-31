package hekr.me.iotos.softgateway.northProxy.device;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  @JsonProperty("DeviceId")
  private String deviceId;
  /** 设备名称 */
  @JsonProperty("Name")
  private String name;
  /** 设备类别Id */
  @JsonProperty("DeviceTypeId")
  private String deviceTypeId;
  /** 区域Id */
  @JsonProperty("AreaId")
  private String areaId;
  /** 客户编号 */
  @JsonProperty("CustomerCode")
  private String customerCode;
  /** 父设备Id */
  @JsonProperty("ParentId")
  private String parentId;
}
