package me.hekr.iotos.softgateway.network.http;

/** @author iotos */
public interface PageableResponseParser<R extends PageableResponse<T>, T> {
  /**
   * 解析 response
   *
   * @param response 请求结果
   * @return 请求结果
   */
  HttpPageResponse<R, T> parse(HttpResponse response);
}
