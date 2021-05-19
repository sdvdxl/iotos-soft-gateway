package me.hekr.iotos.softgateway.network.http;

import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页请求结果
 *
 * @author iotos
 */
public class HttpPageResponse<R extends PageableResponse<T>, T> {

  /** response 整体结果 */
  @Setter @Getter private R result;

  public HttpPageResponse() {}

  public HttpPageResponse(R r) {
    this.result = r;
  }

  public static <R extends PageableResponse<T>, T> HttpPageResponse<R, T> wrap(R r) {
    return new HttpPageResponse<>(r);
  }

  public Collection<T> getItems() {
    return result == null ? Collections.emptyList() : result.getItems();
  }
}
