package me.hekr.iotos.softgateway.core.klink;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>GetTopoResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
public class GetTopoResp extends KlinkResp {

  private static final long serialVersionUID = -4617625211601039076L;
  private List<Dev> subs;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.GET_TOPO_RESP.getAction();
  }
}
