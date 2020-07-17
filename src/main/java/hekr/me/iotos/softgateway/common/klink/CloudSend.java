package hekr.me.iotos.softgateway.common.klink;

import hekr.me.iotos.softgateway.common.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CloudSend extends KlinkDev {

  /** 物模型数据 */
  private ModelData data;

  @Override
  public String getAction() {
    return Action.CLOUD_SEND.getAction();
  }
}
