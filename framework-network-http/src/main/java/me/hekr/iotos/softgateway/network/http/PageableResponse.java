package me.hekr.iotos.softgateway.network.http;

import java.util.List;

/** @author iotos */
public interface PageableResponse<T> {

  /**
   * 获取分页的元素列表
   *
   * @return 分页的元素列表
   */
  List<T> getItems();
}
