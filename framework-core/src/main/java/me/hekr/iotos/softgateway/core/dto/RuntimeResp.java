package me.hekr.iotos.softgateway.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RuntimeResp {
  /** 设备Id */
  @JsonProperty("DeviceId")
  private String deviceId;
  /** 网络节点设备Id */
  @JsonProperty("NetPointId")
  private String netPointId;
  /** 设备属性序号 */
  @JsonProperty("DefineIndex")
  private int defineIndex;
  /** 位号说明 */
  @JsonProperty("Memo")
  private String memo;
  /** 位号 */
  @JsonProperty("DIndex")
  private int dIndex;
  /** 能耗类别编码 */
  @JsonProperty("EnergyType")
  private String energyType;
  /** 位号的实时数据值 */
  @JsonProperty("Value")
  private double value;
  /** 错误号，0为正确数据，非0为错误数据 */
  @JsonProperty("ErrorCode")
  private int errorCode;
}
