package me.hekr.iotos.softgateway.network.http;

/** @author iotos */
public interface ResponseParser<T> {

  /**
   * 从 response 解析成对象
   *
   * @param response response
   * @return 解析结果
   */
  T parse(HttpResponse response);
}
