package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.RuntimeResp;
import hekr.me.iotos.softgateway.common.dto.TokenResp;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** */
@Slf4j
@RestController
@RequestMapping(value = "/api/bi")
public class HttpController {

  @GetMapping("/Login")
  public BaseResp<TokenResp> login(@RequestParam String aid, @RequestParam String key) {
    BaseResp<TokenResp> tokenRespBaseResp = new BaseResp<>();
    if ("test".equals(aid) && "111111".equals(key)) {
      tokenRespBaseResp.setInfo("请求(或处理)成功");
      tokenRespBaseResp.setStatusCode(200);
      TokenResp tokenResp = new TokenResp();
      tokenResp.setApplicationId("test");
      tokenResp.setId("c9648f504179bdd2");
      tokenResp.setKey("b62987f93ced7dd2");
      tokenRespBaseResp.setData(tokenResp);
    } else {
      tokenRespBaseResp.setInfo("失败");
      tokenRespBaseResp.setStatusCode(500);
    }
    return tokenRespBaseResp;
  }

  @GetMapping("/GetRuntimeData")
  public BaseResp<List<RuntimeResp>> getRuntimeData(@RequestParam String[] deviceIds) {
    List<RuntimeResp> respList = new ArrayList<>();
    RuntimeResp dev1_101 = new RuntimeResp();
    dev1_101.setDefineIndex(101);
    dev1_101.setDeviceId("12e8ef8b5332fa2d");
    dev1_101.setValue(23.23);
    RuntimeResp dev1_102 = new RuntimeResp();
    dev1_102.setDefineIndex(102);
    dev1_102.setDeviceId("12e8ef8b5332fa2d");
    dev1_102.setValue(233.23);
    RuntimeResp dev1_103 = new RuntimeResp();
    dev1_103.setDefineIndex(103);
    dev1_103.setDeviceId("12e8ef8b5332fa2d");
    dev1_103.setValue(253.23);
    respList.add(dev1_101);
    respList.add(dev1_102);
    respList.add(dev1_103);
    BaseResp<List<RuntimeResp>> listBaseResp = new BaseResp<>();
    listBaseResp.setData(respList);
    return listBaseResp;
  }
}
