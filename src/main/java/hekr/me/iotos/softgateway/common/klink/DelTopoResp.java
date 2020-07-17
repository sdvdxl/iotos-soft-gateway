package hekr.me.iotos.softgateway.common.klink;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DelTopoResp extends KlinkResp {

  /** 子设备 */
  private Dev sub;

  public DelTopoResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  public DelTopoResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.DEL_TOPO_RESP.getAction();
  }
}
