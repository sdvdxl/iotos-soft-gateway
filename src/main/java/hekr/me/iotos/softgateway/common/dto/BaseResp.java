package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/29
 */
@Data
public class BaseResp<T> {
  private int StatusCode;
  private String Info;
  private T Data;
}
