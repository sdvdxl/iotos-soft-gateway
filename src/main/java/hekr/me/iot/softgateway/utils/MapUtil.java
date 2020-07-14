package hekr.me.iot.softgateway.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {
  /**
   * @Title: objectToMap @Description: 将object转换为map，默认不保留空值
   *
   * @param @param obj
   * @return Map<String,Object> 返回类型
   * @throws
   */
  public static Map<String, Object> objectToMap(Object obj) {

    Map<String, Object> map = new HashMap<String, Object>();
    map = objectToMap(obj, false);
    return map;
  }

  public static Map<String, Object> objectToMap(Object obj, boolean keepNullVal) {
    if (obj == null) {
      return null;
    }

    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Field[] declaredFields = obj.getClass().getDeclaredFields();
      for (Field field : declaredFields) {
        field.setAccessible(true);
        if (keepNullVal) {
          map.put(field.getName(), field.get(obj));
        } else {
          if (field.get(obj) != null && !"".equals(field.get(obj).toString())) {
            map.put(field.getName(), field.get(obj));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }
}
