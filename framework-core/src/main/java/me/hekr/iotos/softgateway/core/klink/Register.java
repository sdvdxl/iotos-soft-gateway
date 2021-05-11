package me.hekr.iotos.softgateway.core.klink;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hekr.iotos.softgateway.core.enums.Action;

@EqualsAndHashCode(callSuper = true)
@Data
public class Register extends KlinkDev {

  private static final long serialVersionUID = 6099206158766831129L;
  private String random;
  private String hashMethod;
  private String sign;
  private String name;
  private String imei;
  private String batchName;

  @Override
  public String getAction() {
    return Action.REGISTER.getAction();
  }
}
