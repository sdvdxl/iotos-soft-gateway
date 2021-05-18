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
public class HttpPageResponse<P, T> {

  /** 分页元素 */
  @Setter private List<T> items;
  /** response 整体结果 */
  @Setter @Getter private P result;
  public HttpPageResponse() {}

  public HttpPageResponse(P p, List<T> t) {
    this.result = p;
    this.items = t;
  }

  public static <P, T> HttpPageResponse<P, T> wrap(P p, List<T> t) {
    return new HttpPageResponse<>(p, t);
  }

  public List<T> getItems() {
    return items == null ? Collections.emptyList() : items;
  }
}
