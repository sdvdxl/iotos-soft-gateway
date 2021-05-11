package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** @author iotos */
@ToString(callSuper = true)
@Getter
@Setter
public class SuModelData extends ModelData {

  private static final long serialVersionUID = -136551194596662462L;
  private String pk;
  private String devId;
}
