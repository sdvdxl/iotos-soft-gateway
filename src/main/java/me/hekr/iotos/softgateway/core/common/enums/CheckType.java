package me.hekr.iotos.softgateway.core.common.enums;

import lombok.Getter;

@Getter
public enum CheckType {
  /** 范围校验 */
  RANGE("range"),

  /** 枚举校验 */
  ENUM("enum"),

  /** 长度校验，用于字符串 */
  LENGTH("length");

  private final String type;

  CheckType(String type) {
    this.type = type;
  }
}
