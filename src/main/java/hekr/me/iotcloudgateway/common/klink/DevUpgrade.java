package hekr.me.iotcloudgateway.common.klink;

import hekr.me.iotcloudgateway.common.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DevUpgrade extends KlinkDev {

  private String url;
  private String md5;
  private String version;
  private String type;

  @Override
  public String getAction() {
    return Action.DEV_UPGRADE.getAction();
  }
}
