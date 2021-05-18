package me.hekr.iotos.softgateway.network.http;

/**
 * 分页请求
 *
 * @author iotos
 */
public abstract class HttpRequestPageable<R> {
  int curPage = 0;
  int pageSize = 10;

  /** 默认分页 curPage=0; pageSize=10; */
  public HttpRequestPageable() {}

  public HttpRequestPageable(int curPage, int pageSize) {
    this.curPage = curPage;
    this.pageSize = pageSize;
  }

  /**
   * 通过分页信息构造一个 httpRequest
   *
   * @param curPage 当前页码， 下次查询的页码，第一次传入是 初始化的页面，后面每次调用会+1
   * @param pageSize 每页大小
   * @return 构造的 HttpRequest
   */
  public abstract HttpRequest buildRequest(int curPage, int pageSize);

  /**
   * 是否还有更多元素
   *
   * @param resp 结果
   * @return true 还有剩余分页数据；false 没有分页数据
   */
  public abstract boolean hasMore(R resp);
}
