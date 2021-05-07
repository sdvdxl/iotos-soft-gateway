package me.hekr.iotos.softgateway.core.dto;

import lombok.Data;

/**
 * @author iotos
 * @date 2020/7/31
 */
@Data
public class EnergyStatDataResp {

  private String deviceId;
  /** 设备属性序号，该数据对应设备类别中的属性定义顺序号。 */
  private int DefineIndex;
  /** 当前读数。 */
  private double CurValue;
  /** 上期读数。 */
  private double PreValue;
  /** 本期用量。 */
  private double CurrentAmount;
  /** 上期用量。 */
  private double LastAmount;
  /** 去年同期用量。 */
  private double CurrentAmountOfLastYear;
  /** 环比。 */
  private double ComparedWith;
  /** 同比。 */
  private double ComparedWithYear;
}
