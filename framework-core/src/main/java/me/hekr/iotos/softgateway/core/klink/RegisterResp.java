package me.hekr.iotos.softgateway.core.klink;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

/**
 * <p>RegisterResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class RegisterResp extends KlinkResp {

  /** {@inheritDoc} */
  @Override
  public boolean isSuccess() {
    return super.isSuccess() || code == ErrorCode.DEV_ID_EXIST.getCode();
  }

  private static final long serialVersionUID = -6728983939835762139L;
  private String devSecret;

  /**
   * <p>Constructor for RegisterResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   * @param desc a {@link java.lang.String} object.
   */
  public RegisterResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.REGISTER_RESP.getAction();
  }
}
