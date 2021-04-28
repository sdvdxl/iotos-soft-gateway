package me.hekr.iotos.softgateway.common.dto;

import lombok.Data;

@Data
public class EnergyMeterResp {
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
  public int DefineIndex;
  /** 计量用标志，0-普通（总值），1-峰值，2-谷值，3-平值 */
  public int MeterFlag;
  /** 当前读数 */
  public double CurValue;
  /** 上期读数 */
  public double PreValue;
  /** 调整读数，换表时需要 */
  public double AdjustValue;
  /** 本期用量 */
  public double Quantity;
}