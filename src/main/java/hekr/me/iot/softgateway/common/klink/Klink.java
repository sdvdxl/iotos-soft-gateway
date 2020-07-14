package hekr.me.iot.softgateway.common.klink;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class Klink implements Serializable {

  @JsonIgnore public static final String CMD = "cmd";
  private static final long serialVersionUID = -4341021820638489039L;
  protected String action;
  protected long msgId;

  /** 定制前置机使用，发送原始数据 */
  protected String sysCustomRaw;
}
