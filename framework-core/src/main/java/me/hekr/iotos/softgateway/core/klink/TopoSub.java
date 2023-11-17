package me.hekr.iotos.softgateway.core.klink;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
/**
 * <p>TopoSub class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopoSub {
  protected String pk;
  protected String devId;
  private String random;
  private String hashMethod;
  private String sign;
}
