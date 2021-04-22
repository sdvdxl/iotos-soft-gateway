package me.hekr.iotos.softgateway.pluginAsClient.http;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.config.ProxyConfig;
import me.hekr.iotos.softgateway.common.constant.Constants;
import me.hekr.iotos.softgateway.common.dto.BaseResp;
import me.hekr.iotos.softgateway.common.dto.EnergyStatDataReq;
import me.hekr.iotos.softgateway.common.dto.EnergyStatDataResp;
import me.hekr.iotos.softgateway.common.dto.RuntimeReq;
import me.hekr.iotos.softgateway.common.dto.RuntimeResp;
import me.hekr.iotos.softgateway.common.dto.TokenResp;
import me.hekr.iotos.softgateway.utils.JsonUtil;
import me.hekr.iotos.softgateway.utils.MapUtil;
import me.hekr.iotos.softgateway.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpClient {
  @Autowired ProxyConfig proxyConfig;

  private static String tokenId;
  private static String tokenKey;

  /** 登陆请求获取token */
  //  @Scheduled(fixedDelay = 20 * 1000)
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
    String signature = SignUtil.getSignature(timeMillis, Constants.NONCE, tokenKey, params);
    headerParams.put("timestamp", timeMillis);
    headerParams.put("signature", signature);
    return headerParams;
  }

  public BaseResp<List<EnergyStatDataResp>> getEnergyStatData(EnergyStatDataReq energyStatDataReq) {
    Map<String, Object> energyMeterCtrl = MapUtil.objectToMap(energyStatDataReq);
    Map<String, String> headers = getHeaders(HttpUtils.getParams(energyMeterCtrl));
    if (headers == null) {
      log.warn("请求[GetEnergyStatData]失败，未完成登陆校验");
      return null;
    }
    String url = proxyConfig.getHTTP_URL() + "/api/bi/GetEnergyStatData";
    HttpUtils httpUtils = new HttpUtils();
    byte[] bytes = httpUtils.get(url, headers, energyMeterCtrl);
    return JsonUtil.fromBytes(bytes, new TypeReference<BaseResp<List<EnergyStatDataResp>>>() {});
  }

  public BaseResp<List<RuntimeResp>> getRuntimeData(RuntimeReq runtimeReq) {
    Map<String, Object> runtimeDataCtrl = MapUtil.objectToMap(runtimeReq);
    Map<String, String> headers = getHeaders(HttpUtils.getParams(runtimeDataCtrl));
    if (headers == null) {
      log.warn("请求[GetRuntimeData]失败，未完成登陆校验");
      return null;
    }
    String url = proxyConfig.getHTTP_URL() + "/api/bi/GetRuntimeData";
    HttpUtils httpUtils = new HttpUtils();
    byte[] bytes = httpUtils.get(url, headers, runtimeDataCtrl);
    return JsonUtil.fromBytes(bytes, new TypeReference<BaseResp<List<RuntimeResp>>>() {});
  }
}
