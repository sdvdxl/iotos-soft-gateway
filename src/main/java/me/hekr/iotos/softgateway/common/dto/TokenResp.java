package me.hekr.iotos.softgateway.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/29
 */
@Data
public class TokenResp {
  /** 应用程序ID */
  @JsonProperty("ApplicationId")
  private String applicationId;
  /** Token ID */
  @JsonProperty("Id")
  private String id;
  /** Token Key */
  @JsonProperty("Key")
  private String key;
  /** Token 过期时间 */
  @JsonProperty("ExpireTime")
  private Date expireTime;
}