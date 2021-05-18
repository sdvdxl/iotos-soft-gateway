package me.hekr.iotos.softgateway.network.http;

/**
 * http 异常
 *
 * @author IoTOS
 */
public class HttpException extends RuntimeException {

  public HttpException(HttpRequest request, Throwable t) {}

  public HttpException(String message) {
    super(message);
  }

  public HttpException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpException(Throwable cause) {
    super(cause);
  }

  public HttpException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public HttpException(HttpRequest request, HttpResponse response, Throwable throwable) {
    super("request:" + request + ", response:" + response, throwable);
  }
}
