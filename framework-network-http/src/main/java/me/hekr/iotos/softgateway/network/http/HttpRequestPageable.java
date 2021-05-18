package me.hekr.iotos.softgateway.network.http;

import okhttp3.Request;

/**
 * 分页请求
 *
 * @author iotos
 */
public interface HttpRequestPageable<R> {
  HttpRequest buildPageRequest(int currentPage, int pageSize);

  /**
   * 是否还有更多元素
   *
   * @param resp 结果
   * @return true 还有剩余分页数据；false 没有分页数据
   */
  boolean hasMore(R resp);
}
