package hekr.me.iotcloudgateway.klink;

import hekr.me.iotcloudgateway.enums.Action;
import hekr.me.iotcloudgateway.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class GetConfigResp extends KlinkResp {
  private String url;
  private String md5;

  public GetConfigResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.GET_CONFIG_RESP.getAction();
  }
}
