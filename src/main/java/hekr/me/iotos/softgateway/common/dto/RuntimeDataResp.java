package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/29
 */
@Data
public class RuntimeDataResp {
  /** 设备Id */
  private String DeviceId;
  /** 网络节点设备Id */
  private String NetPointId;
  /** 设备属性序号 */
  private int DefineIndex;
  /** 位号说明 */
  private String Memo;
  /** 位号 */
  private int DIndex;
  /** 能耗类别编码 */
  private String EnergyType;
  /** 位号的实时数据值 */
  private double Value;
  /** 错误号，0为正确数据，非0为错误数据 */
  private int ErrorCode;
}
