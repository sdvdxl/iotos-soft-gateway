package me.hekr.iotos.softgateway.common.constant;

import lombok.Getter;

/** HTTP 请求回复状态码 */
@Getter
public enum StatusCode {
  SUCCESS(200, "成功"),
  INTERNAL_ERROR(500, "内部错误"),
  BAD_REQUEST(400, "请求参数不完整或不正确"),
  UNAUTHORIZED(401, "未授权标识"),
  FORBIDDEN(403, "请求TOKEN失效"),
  METHOD_NOT_ALLOWED(405, "HTTP请求类型不合法"),
  NOT_ACCEPTABLE(406, "HTTP请求不合法，请求参数可能被篡改"),
  AUTHENTICATION_REQUIRED(407, "HTTP请求不合法，该URL已经失效");

  private final int code;
  private final String decs;

  StatusCode(int code, String decs) {
    this.code = code;
    this.decs = decs;
  }
}
