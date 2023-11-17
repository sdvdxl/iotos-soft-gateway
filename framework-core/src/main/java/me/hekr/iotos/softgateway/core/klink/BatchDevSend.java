package me.hekr.iotos.softgateway.core.klink;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

/**
 * <p>BatchDevSend class.</p>
 *
 * @version $Id: $Id
 */
@Getter
@Setter
@ToString(callSuper = true)
public class BatchDevSend extends KlinkDev {

  private List<SuModelData> data;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.BATCH_DEV_SEND.getAction();
  }
}
