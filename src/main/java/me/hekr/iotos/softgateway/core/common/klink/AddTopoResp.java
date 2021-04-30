package me.hekr.iotos.softgateway.core.common.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.enums.ErrorCode;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class AddTopoResp extends KlinkResp {

  /** 子设备 */
  private Dev sub;

  public AddTopoResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  public AddTopoResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.ADD_TOPO_RESP.getAction();
  }
}
