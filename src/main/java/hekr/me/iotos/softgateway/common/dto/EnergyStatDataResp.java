package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/31
 */
@Data
public class EnergyStatDataResp {
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
