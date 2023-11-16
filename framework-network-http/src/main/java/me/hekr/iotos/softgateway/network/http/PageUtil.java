package me.hekr.iotos.softgateway.network.http;

import cn.hutool.core.collection.CollectionUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 * @author du
 */
@Data
public class PageUtil<T> implements Serializable {

  private static final long serialVersionUID = 5926676981594352187L;
  /** T 总条数 */
  private long totalElements;

  /** 当前页码 */
  private int page;

  /** 分页大小 */
  private int size;
  /** 内容 */
  private List<T> content;

  public static <T> PageUtil<T> of(int page, int size) {
    PageUtil<T> pagec = new PageUtil<>();
    pagec.page = page;
    pagec.size = size;
    return pagec;
  }

  public static <T> List<T> getAll(int initPage, int pageSize, PageCall<T> request) {
    List<T> result = new ArrayList<>();
    for (int i = initPage; ; i++) {
      List<T> list = request.onPage(i, pageSize);
      if (CollectionUtil.isEmpty(list)){
        break;
      }

      result.addAll(list);
      if (list.size() < pageSize) {
        break;
      }
    }
    return result;
  }

  public static <T> void onPage(int initPage, int pageSize, PageCall<T> request) {
    for (int i = initPage; ; i++) {
      List<T> list = request.onPage(i, pageSize);
      if (CollectionUtil.isEmpty(list) || list.size() < pageSize) {
        break;
      }
    }
  }

  /**
   * 总页数
   *
   * @return 总页数
   */
  public int getTotalPages() {
    return this.size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) this.size);
  }

  public List<T> getContent() {
    return content == null ? Collections.emptyList() : content;
  }

  public interface PageCall<T> {

    /**
     * 分页进行的时候，自定义处理
     *
     * @param page 当前页码，从0开始
     * @param size 分页大小
     * @return List
     */
    List<T> onPage(int page, int size);
  }
}
