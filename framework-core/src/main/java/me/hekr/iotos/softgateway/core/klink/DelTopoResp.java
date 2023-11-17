package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

@Getter
/**
 * <p>DelTopoResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DelTopoResp extends KlinkResp {

  /** 子设备 */
  private Dev sub;

  /**
   * <p>Constructor for DelTopoResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   * @param desc a {@link java.lang.String} object.
   */
  public DelTopoResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  /**
   * <p>Constructor for DelTopoResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   */
  public DelTopoResp(ErrorCode errorCode) {
    super(errorCode);
  }

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEL_TOPO_RESP.getAction();
  }
}
