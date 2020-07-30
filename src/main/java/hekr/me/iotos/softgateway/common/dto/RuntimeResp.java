package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

@Data
public class RuntimeResp {
  /** 设备Id */
  private String DeviceId;
  /** 网络节点设备Id */
  private String NetPointId;
  /** 设备属性序号 */
  private String DefineIndex;
  /** 位号说明 */
  private String Memo;
  /** 位号 */
  private String DIndex;
  /** 能耗类别编码 */
  private String EnergyType;
  /** 位号的实时数据值 */
  private String Value;
  /** 错误号，0为正确数据，非0为错误数据 */
  private String ErrorCode;
}
