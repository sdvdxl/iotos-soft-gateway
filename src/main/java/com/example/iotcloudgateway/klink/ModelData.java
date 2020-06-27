package com.example.iotcloudgateway.klink;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import lombok.Data;

/**
 * @author du
 *     <p>物模型数据
 */
@Data
public class ModelData implements Serializable {

  private static final long serialVersionUID = 5838451843005203760L;
  /** 指令 */
  private String cmd;
  /** 参数数据 */
  @JsonInclude(Include.NON_NULL)
  private Map<String, Object> params;

  public Map<String, Object> getParams() {
    return params == null ? Collections.emptyMap() : params;
  }
}
