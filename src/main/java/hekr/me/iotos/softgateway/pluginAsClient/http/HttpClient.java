package hekr.me.iotos.softgateway.pluginAsClient.http;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.utils.MapUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpClient {
  @Autowired ProxyConfig proxyConfig;

//  public byte[] gateControl(GateControlReq gateControlReq) {
//    Map<String, Object> gateControl = MapUtil.objectToMap(gateControlReq);
//    Map<String, String> headerParams = new HashMap<>();
//    headerParams.put("Content-Type", "application/json");
//    String url = proxyConfig.getHTTP_URL() + "/GetChannelInfo";
//    HttpUtils httpUtils = new HttpUtils();
//    return httpUtils.post(url, headerParams, gateControl);
//  }
}
