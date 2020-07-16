package hekr.me.iot.softgateway.common.dto;

import lombok.Data;

@Data
public class ChargeReq {

  /** 必选，记录ID */
  private String recordID;
  /** 必选，车牌号码 */
  private String carCode;
  /** 必选，进场时间（格式yyyy-MM-dd HH:mm:ss） */
  private String inTime;
  /** 必选，缴费时间（格式yyyy-MM-dd HH:mm:ss） */
  private String payTime;
  /** 必选，车场ID */
  private String parkID;
  /** 车辆本次进场出场标识(amountType=0或3不为空) */
  private String GUID;

  /** 必选，总金额（单位：分）=（实付金额+减免金额） */
  private int chargeMoney;
  /** 必选，实付金额（单位：分） */
  private int paidMoney;
  /** 必选，减免金额（单位：分） */
  private int JMMoney;
  /** 必选，详见”附录2 收费方式（chargeType）字典” */
  private int chargeType;
  /** 必选，详见”附录2 收费来源（chargeSource）字典” */
  private String chargeSource;
  /** 必选，费用类型（停车费：0，续费：1，消费：3，充值：4） */
  private int amountType;
}
