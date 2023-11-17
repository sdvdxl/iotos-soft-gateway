package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>CloudSend class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
public class CloudSend extends KlinkDev {

  /** 物模型数据 */
  private ModelData data;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.CLOUD_SEND.getAction();
  }
}
