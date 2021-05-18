package me.hekr.iotos.softgateway.network.http;

/** @author iotos */
public interface ResponseParser {

  /**
   * 解析 response
   *
   * @param response 请求结果
   * @return 请求结果
   */
  HttpPageResponse parse(HttpResponse response);
}
