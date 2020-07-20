package hekr.me.iotos.softgateway.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ChargeSource {
  /** 现金 */
  CASH(0, "现金"),
  /** 电子账户 */
  E_ACCOUNT(1, "电子账户"),
  /** 支付宝 */
  ALIPAY(2, "支付宝"),
  /** 微信支付 */
  WECHAT(3, "微信支付"),
  /** 平台支付 */
  PLATFORM(4, "平台支付"),
  /** 喵街 */
  MIAOJI(5, "喵街"),
  /** 闪付 */
  QUICK_PASS(6, "闪付"),
  /** 支付宝刷卡（用户二维码被扫） */
  ALIPAY_QR(7, "支付宝刷卡（用户二维码被扫）"),
  /** 微信刷卡（用户二维码被扫） */
  WECHAT_QR(8, "微信刷卡（用户二维码被扫）"),
  /** 银联 */
  UNION_PAY(9, "银联"),
  /** 苹果支付 */
  APPLE_PAY(10, "苹果支付"),
  /** 协商收费 */
  NEGOTIATION(11, "协商收费"),
  /** 禁用 */
  FORBIDDEN(12, "禁用"),
  /** 微信公众号 */
  WECHAT_OFFICIAL_ACCOUNT(13, "微信公众号"),
  /** 立方钱包 */
  REFORME(14, "立方钱包"),
  /** 积分 */
  ACCUMULATE_POINTS(15, "积分"),
  /** 交通卡 */
  TRANSPORTATION_CARD(100, "交通卡"),
  /** 优财收费 */
  YOUCAI_CHARGE(101, "优财收费"),
  /** 未结算 */
  UNPAID(200, "未结算"),
  /** 服务窗支付宝 */
  SERVICE_WINDOW_ALIPAY(2000, "服务窗支付宝"),
  /** 银联权益平台 */
  UNIONPAY_EQUALITY_PLATFORM(5003, "银联权益平台"),
  /** 充值卡 */
  PREPAID_CARD(8000, "充值卡"),
  /** 路侧 */
  ROADSIDE(9888, "路侧"),
  ;

  private int value;

  private String desc;

  ChargeSource(int value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  @JsonValue
  public String getDesc() {
    return desc;
  }

  public static ChargeSource valueOf(int value) {
    for (ChargeSource type : ChargeSource.values()) {
      if (type.getValue() == value) {
        return type;
      }
    }
    return null;
  }
}
