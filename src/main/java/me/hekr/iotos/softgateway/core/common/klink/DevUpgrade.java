package me.hekr.iotos.softgateway.core.common.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.common.enums.Action;

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
