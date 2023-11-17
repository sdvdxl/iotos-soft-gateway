package me.hekr.iotos.softgateway.core.enums;

import lombok.Getter;

/**
 * <p>DataFormat class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
public enum DataFormat {
  /** 标准格式klink */
  KLINK("KLink(标准格式)"),
  /** 自定义 */
  CUSTOM("自定义");

  @Getter private final String desc;

  DataFormat(String desc) {
    this.desc = desc;
  }
}
