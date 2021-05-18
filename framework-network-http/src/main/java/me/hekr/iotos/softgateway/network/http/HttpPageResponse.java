package me.hekr.iotos.softgateway.network.http;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页请求结果
 *
 * @author iotos
 */
public class HttpPageResponse<R extends PageableResponse<T>, T> {

  /** 分页元素 */
  @Setter private List<T> items;
  /** response 整体结果 */
  @Setter @Getter private R result;

  public HttpPageResponse() {}

  public HttpPageResponse(R r, List<T> t) {
    this.result = r;
    this.items = t;
  }

  public static <R extends PageableResponse<T>, T> HttpPageResponse<R, T> wrap(R r) {
    return new HttpPageResponse<>(r, r.getItems());
  }

  public List<T> getItems() {
    return items == null ? Collections.emptyList() : items;
  }
}
