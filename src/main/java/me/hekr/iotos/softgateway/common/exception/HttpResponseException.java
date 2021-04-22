package me.hekr.iotos.softgateway.common.exception;

/**
 * @author jiatao
 * @date 2020/7/20
 */
public class HttpResponseException extends RuntimeException {
  private final int code;

  public HttpResponseException(int code, String msg) {
    super(msg);
    this.code = code;
  }
}
