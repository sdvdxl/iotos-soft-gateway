package me.hekr.iotos.softgateway.core.klink;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>ModelData class.</p>
 *
 * @author iotos
 * @version $Id: $Id
 */
@Data
@Accessors(chain = true)
public class ModelData implements Serializable {

  private static final long serialVersionUID = 5838451843005203760L;
  /** 指令 */
  private String cmd;
  /** 参数数据 */
  @JsonInclude(Include.NON_NULL)
  private Map<String, Object> params;

  /**
   * <p>cmd.</p>
   *
   * @param cmd a {@link java.lang.String} object.
   * @return a {@link me.hekr.iotos.softgateway.core.klink.ModelData} object.
   */
  public static ModelData cmd(String cmd) {
    ModelData modelData = new ModelData();
    modelData.setCmd(cmd);
    return modelData;
  }

  /**
   * <p>Getter for the field <code>params</code>.</p>
   *
   * @return a {@link java.util.Map} object.
   */
  public Map<String, Object> getParams() {
    return params == null ? Collections.emptyMap() : params;
  }

  /**
   * <p>getParam.</p>
   *
   * @param param a {@link java.lang.String} object.
   * @param <T> a T object.
   * @return a T object.
   */
  public <T> T getParam(String param) {
    return params == null ? null : (T) params.get(param);
  }

  /**
   * <p>param.</p>
   *
   * @param param a {@link java.lang.String} object.
   * @param value a {@link java.lang.Object} object.
   * @return a {@link me.hekr.iotos.softgateway.core.klink.ModelData} object.
   */
  public ModelData param(String param, Object value) {
    if (params == null) {
      params = new HashMap<>(10);
    }
    params.put(param, value);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "cmd='" + cmd + '\'' + ", params=" + params;
  }
}
