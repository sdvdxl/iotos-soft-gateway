package hekr.me.iotos.softgateway.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author jiatao
 * @date 2020/7/20
 */
@Getter
public enum ChargeType {
  SENTRY_BOX(0, "岗亭收费"),
  TICKET_BOX(1, "票箱收费"),
  PAYMENT_MACHINE(2, "自助缴费机"),
  HANDHELD_MACHINE(3, "手持机"),
  APP(4, "手机App"),
  INTERFACE_CHARGE(11, "接口收费"),
  CENTRAL_PAYMENT(1000, "中央缴费"),
  ROBOT(1007, "机器人"),
  INQUIRY_MACHINE(1008, "查询机"),
  ;

  private int value;
  private String desc;

  ChargeType(int value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  @JsonValue
  public String getDesc() {
    return desc;
  }

  public static ChargeType valueOf(int value) {
    for (ChargeType type : ChargeType.values()) {
      if (type.getValue() == value) {
        return type;
      }
    }
    return null;
  }
}
