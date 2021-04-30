package me.hekr.iotos.softgateway.core.common.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class KlinkDev extends Klink {

  private static final long serialVersionUID = 78156295896577172L;
  protected String pk;
  protected String devId;
  protected String devSecret;
  protected String productSecret;
  protected String name;
}
