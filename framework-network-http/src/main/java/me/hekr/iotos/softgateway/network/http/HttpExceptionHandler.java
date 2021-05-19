package me.hekr.iotos.softgateway.network.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常处理器
 *
 * @author IoTOS
 */
public interface HttpExceptionHandler {
  Logger log = LoggerFactory.getLogger(HttpExceptionHandler.class);
  /** 抛出异常 */
  HttpExceptionHandler THROW_HANDLER =
      (request, t) -> {
        throw new HttpException(request, t);
      };

  /** 默认打印异常 */
  HttpExceptionHandler LOG_HANDLER =
      (request, t) -> {
        log.error("request:" + request + ", error:" + t.getMessage(), t);
        return new HttpResponse();
      };
  /**
   * 异常发生的时候
   *
   * @param request 请求
   * @param t 异常
   * @return 自定义 response
   */
  HttpResponse onException(HttpRequest request, Throwable t);
}
