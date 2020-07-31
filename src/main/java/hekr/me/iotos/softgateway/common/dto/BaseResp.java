package hekr.me.iotos.softgateway.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/29
 */
@Data
public class BaseResp<T> {
  @JsonProperty("StatusCode")
  private int statusCode;

  @JsonProperty("Info")
  private String info;

  @JsonProperty("Data")
  private T data;
}
