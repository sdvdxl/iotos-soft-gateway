package iot.example.iotcloudgateway;

import com.fasterxml.jackson.core.type.TypeReference;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.TokenResp;
import hekr.me.iotos.softgateway.utils.JsonUtil;

public class IotCloudGatewayApplicationTests {
  public static void main(String[] args) {
    BaseResp<TokenResp> tokenRespBaseResp = new BaseResp<>();
    tokenRespBaseResp.setInfo("请求(或处理)成功");
    tokenRespBaseResp.setStatusCode(200);
    TokenResp tokenResp = new TokenResp();
    tokenResp.setApplicationId("test");
    tokenResp.setId("c9648f504179bdd2");
    tokenResp.setKey("b62987f93ced7dd2");
    tokenRespBaseResp.setData(tokenResp);

    String s = JsonUtil.toJson(tokenRespBaseResp);
    BaseResp<TokenResp> o = JsonUtil.fromJson(s, new TypeReference<BaseResp<TokenResp>>() {});
    System.out.println(o);
  }
}
