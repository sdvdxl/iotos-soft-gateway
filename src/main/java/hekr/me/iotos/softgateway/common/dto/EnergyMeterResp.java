package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

@Data
public class EnergyMeterResp {

  private String StatusCode;
  /** 返回信息 */
  private String Info;
  /** 返回结果数据，本接口为项目基本信息 */
  private String Data;

  /** 关键字 */
  public String Id;
  /** 设备Id */
  public String DeviceId;
  /** 设备类别Id */
  public String DeviceTypeId;
  /** 所属区域Id */
  public String AreaId;
  /** 所属客户编码 */
  public String CustomerCode;
  /** 数据采集时间 */
  public String OccurDT;
  /** 计量运算时间 */
  public String CreateDT;
  /** 能耗类别编码 */
  public String EnergyType;
  /** 该数据对应设备类别中的属性定义顺序号 */
  public String DefineIndex;
  /** 计量用标志，0-普通（总值），1-峰值，2-谷值，3-平值 */
  public String MeterFlag;
  /** 当前读数 */
  public String CurValue;
  /** 上期读数 */
  public String PreValue;
  /** 调整读数，换表时需要 */
  public String AdjustValue;
  /** 本期用量 */
  public String Quantity;
}
