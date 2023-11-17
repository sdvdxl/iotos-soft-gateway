package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

@Getter
/**
 * <p>DevLogoutResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DevLogoutResp extends KlinkResp {

  private static final long serialVersionUID = 7171237139077526254L;

  /**
   * <p>Constructor for DevLogoutResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   * @param msg a {@link java.lang.String} object.
   */
  public DevLogoutResp(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }

  /**
   * <p>Constructor for DevLogoutResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   */
  public DevLogoutResp(ErrorCode errorCode) {
    super(errorCode);
  }

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEV_LOGOUT_RESP.getAction();
  }
}
