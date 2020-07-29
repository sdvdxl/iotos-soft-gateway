package hekr.me.iotos.softgateway.pluginAsClient.http;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.dto.EnergyMeterReq;
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
  EnergyMeterReq energyMeterReq = new EnergyMeterReq();
  Map<String, Object> energyMeter = MapUtil.objectToMap(energyMeterReq);

  public byte[] energyMeter(EnergyMeterReq energyMeterReq) {
    Map<String, Object> eneryMeterCtrl = MapUtil.objectToMap(energyMeterReq);
    Map<String, String> headerParams = new HashMap<>();
    headerParams.put("Content-Type", "application/json");
    String url = proxyConfig.getHTTP_URL() + "/api/bi/GetEnergyMeterData";
    HttpUtils httpUtils = new HttpUtils();
    return httpUtils.get(url, headerParams, eneryMeterCtrl);
  }
}
