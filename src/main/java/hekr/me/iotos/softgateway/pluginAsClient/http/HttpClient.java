package hekr.me.iotos.softgateway.pluginAsClient.http;

import com.fasterxml.jackson.core.type.TypeReference;
import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.constant.Constants;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.EnergyMeterReq;
import hekr.me.iotos.softgateway.common.dto.EnergyMeterResp;
import hekr.me.iotos.softgateway.common.dto.TokenResp;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import hekr.me.iotos.softgateway.utils.MapUtil;
import hekr.me.iotos.softgateway.utils.SignUtil;
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

  /** 登陆请求获取token */
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
    if (tokenId == null || tokenKey == null) {
      log.warn("登陆请求失败，tokenId={},tokenKet={}", tokenId, tokenKey);
    } else {
      log.info(" 登陆成功，tokenId={},tokenKet={}", tokenId, tokenKey);
    }
  }

  private Map<String, String> getHeaders(String params) {
    if (tokenId == null) {
      log.warn("登陆校验未完成，等待登陆获取token");
      return null;
    }
    Map<String, String> headerParams = new HashMap<>();
    headerParams.put("token", tokenId);
    headerParams.put("nonce", Constants.NONCE);
    String timeMillis = Long.toString(System.currentTimeMillis());
    String signature = SignUtil.GetSignature(timeMillis, Constants.NONCE, tokenKey, params);
    headerParams.put("timestamp", timeMillis);
    headerParams.put("signature", signature);
    return headerParams;
  }

  public void getEnergyMeterData(EnergyMeterReq energyMeterReq) {
    Map<String, Object> energyMeterCtrl = MapUtil.objectToMap(energyMeterReq);
    Map<String, String> headers = getHeaders(HttpUtils.getParams(energyMeterCtrl));
    if (headers == null) {
      log.warn("请求[GetEnergyMeterData]失败，未完成登陆校验");
      return;
    }
    String url = proxyConfig.getHTTP_URL() + "/api/bi/GetEnergyMeterData";
    HttpUtils httpUtils = new HttpUtils();
    byte[] bytes = httpUtils.get(url, headers, energyMeterCtrl);
    BaseResp<EnergyMeterResp> energyMeterResp =
        JsonUtil.fromBytes(bytes, new TypeReference<BaseResp<EnergyMeterResp>>() {});
  }
}
