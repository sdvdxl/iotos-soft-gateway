package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

@Getter
/**
 * <p>AddTopoResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class AddTopoResp extends KlinkResp {

  /** 子设备 */
  private Dev sub;

  /**
   * <p>Constructor for AddTopoResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   * @param desc a {@link java.lang.String} object.
   */
  public AddTopoResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  /**
   * <p>Constructor for AddTopoResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   */
  public AddTopoResp(ErrorCode errorCode) {
    super(errorCode);
  }

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.ADD_TOPO_RESP.getAction();
  }
}
