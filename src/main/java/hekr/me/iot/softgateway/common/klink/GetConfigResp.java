package hekr.me.iot.softgateway.common.klink;

import hekr.me.iot.softgateway.common.enums.Action;
import hekr.me.iot.softgateway.common.enums.ErrorCode;
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
