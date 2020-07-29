package hekr.me.iotos.softgateway.common.dto;

import java.util.Date;
import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/29
 */
@Data
public class TokenResp {
  /** 应用程序ID */
  private String ApplicationId;
  /** Token ID */
  private String Id;
  /** Token Key */
  private String Key;
  /** Token 过期时间 */
  private Date ExpireTime;
}
