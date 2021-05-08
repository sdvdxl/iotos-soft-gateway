package me.hekr.iotos.softgateway.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author iotos
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