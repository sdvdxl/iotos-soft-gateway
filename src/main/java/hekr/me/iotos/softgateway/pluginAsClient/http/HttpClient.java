package hekr.me.iotos.softgateway.pluginAsClient.http;

import com.fasterxml.jackson.core.type.TypeReference;
import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.EnergyMeterReq;
import hekr.me.iotos.softgateway.common.dto.TokenResp;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import hekr.me.iotos.softgateway.utils.MapUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpClient {
  @Autowired ProxyConfig proxyConfig;

  private static String tokenId;
  private static String tokenKey;

  @Scheduled(fixedDelay = 5 * 1000)
  public void login() {
    String url = proxyConfig.getHTTP_URL() + "/api/bi/Login";
    HttpUtils httpUtils = new HttpUtils();
    Map<String, Object> login = new HashMap<>();
    login.put("aid", proxyConfig.getAID());
    login.put("key", proxyConfig.getKEY());
    Map<String, String> headerParams = new HashMap<>();
    byte[] bytes = httpUtils.get(url, headerParams, login);
    BaseResp<TokenResp> tokenResp =
        JsonUtil.fromJson(new String(bytes), new TypeReference<BaseResp<TokenResp>>() {});
    tokenId = tokenResp.getData().getId();
    tokenKey = tokenResp.getData().getKey();
  }

  public byte[] energyMeter(EnergyMeterReq energyMeterReq) {
    Map<String, Object> energyMeterCtrl = MapUtil.objectToMap(energyMeterReq);
    Map<String, String> headerParams = new HashMap<>();
    headerParams.put("Content-Type", "application/json");
    String url = proxyConfig.getHTTP_URL() + "/api/bi/GetEnergyMeterData";
    HttpUtils httpUtils = new HttpUtils();
    return httpUtils.get(url, headerParams, energyMeterCtrl);
  }
}
