package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

@Getter
/**
 * <p>DevLoginResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DevLoginResp extends KlinkResp {

  private String random;
  private String hashMethod;
  private String sign;

  /**
   * <p>Constructor for DevLoginResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   * @param desc a {@link java.lang.String} object.
   */
  public DevLoginResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  /**
   * <p>Constructor for DevLoginResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   */
  public DevLoginResp(ErrorCode errorCode) {
    super(errorCode);
  }

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEV_LOGIN_RESP.getAction();
  }
}
