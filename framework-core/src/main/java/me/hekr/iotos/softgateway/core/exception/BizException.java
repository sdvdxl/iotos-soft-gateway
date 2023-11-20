package me.hekr.iotos.softgateway.core.exception;

/**
 * @author du
 */
public class BizException extends RuntimeException {
  public BizException() {}

  public BizException(String msg) {
    super(msg);
  }
}
