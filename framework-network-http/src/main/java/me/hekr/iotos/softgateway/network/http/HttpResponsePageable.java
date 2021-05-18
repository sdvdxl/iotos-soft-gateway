package me.hekr.iotos.softgateway.network.http;

/** @author iotos */
public interface HttpResponsePageable<T> {

  /**
   * 是否还有更多元素
   *
   * @param resp 结果
   * @return true 还有剩余分页数据；false 没有分页数据
   */
  boolean hasMoreHeaders(T resp);
}
