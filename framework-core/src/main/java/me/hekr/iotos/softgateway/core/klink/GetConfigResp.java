package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

/** @author iotos */
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
