package hekr.me.iotcloudgateway.common.klink;

import hekr.me.iotcloudgateway.common.enums.Action;
import hekr.me.iotcloudgateway.common.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class RegisterResp extends KlinkResp {

  private static final long serialVersionUID = -6728983939835762139L;
  private String devSecret;

  public RegisterResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  @Override
  public String getAction() {
    return Action.REGISTER_RESP.getAction();
  }
}
