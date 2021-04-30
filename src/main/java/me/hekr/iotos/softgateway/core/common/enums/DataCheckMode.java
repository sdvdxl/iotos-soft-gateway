package me.hekr.iotos.softgateway.core.common.enums;

public enum DataCheckMode {
  /** 严格模式，协议参数完全按照协议规定上报或者下发，不能多也不能少 */
  STRICT,
  /** 宽松模式，允许上报或者下发的参数不完全匹配协议中命令中的参数，可以多，可以少 */
  LOOSE,
  /** 允许上报或者下发的参数少于协议规定的，但是不能多 */
  LESS,
  /** 允许上报或者下发的参数多于协议规定的，但是不能少 */
  MORE
}
