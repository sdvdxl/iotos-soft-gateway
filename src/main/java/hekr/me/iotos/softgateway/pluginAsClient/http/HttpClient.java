package hekr.me.iotos.softgateway.pluginAsClient.http;

import hekr.me.iotos.softgateway.common.dto.GateControlReq;
import hekr.me.iotos.softgateway.utils.MapUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.tio.utils.jfinal.P;

@Slf4j
public class HttpClient {
  private static String baseUrl = "http://" + P.get("http.client.connect.host");

  /** 控制道闸 */
  public static void gateControl(GateControlReq gateControlReq) {
    Map<String, Object> gateControl = MapUtil.objectToMap(gateControlReq);
    Map<String, String> headerParams = new HashMap<>();
    headerParams.put("Content-Type", "application/json");
    String url = baseUrl + "/GetChannelInfo";
    HttpUtils httpUtils = new HttpUtils();
    httpUtils.post(url, headerParams, gateControl);
  }
}
