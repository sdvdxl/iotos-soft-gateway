package hekr.me.iotos.softgateway.common.klink;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
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
