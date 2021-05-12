package me.hekr.iotos.softgateway.core.klink;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/** @author iotos */
@Data
public class ModelData implements Serializable {

  private static final long serialVersionUID = 5838451843005203760L;
  /** 指令 */
  private String cmd;
  /** 参数数据 */
  @JsonInclude(Include.NON_NULL)
  private Map<String, Object> params;

  public static ModelData cmd(String cmd) {
    ModelData modelData = new ModelData();
    modelData.setCmd(cmd);
    return modelData;
  }

  public Map<String, Object> getParams() {
    return params == null ? Collections.emptyMap() : params;
  }

  public <T> T getParam(String param) {
    return params == null ? null : (T) params.get(param);
  }

  public ModelData param(String param, Object value) {
    if (params == null) {
      params = new HashMap<>(10);
    }
    params.put(param, value);
    return this;
  }

  @Override
  public String toString() {
    return "cmd='" + cmd + '\'' + ", params=" + params;
  }
}
