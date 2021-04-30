package me.hekr.iotos.softgateway.core.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RuntimeReq {
  /** 设备Id */
  @JsonProperty("DeviceIds")
  private String deviceIds;
}
