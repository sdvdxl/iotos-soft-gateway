package hekr.me.iotcloudgateway.common.klink;

import hekr.me.iotcloudgateway.common.enums.Action;
import hekr.me.iotcloudgateway.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DevLogoutResp extends KlinkResp {

  private static final long serialVersionUID = 7171237139077526254L;

  public DevLogoutResp(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }

  public DevLogoutResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.DEV_LOGOUT_RESP.getAction();
  }
}
