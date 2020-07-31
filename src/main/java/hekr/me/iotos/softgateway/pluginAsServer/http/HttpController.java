package hekr.me.iotos.softgateway.pluginAsServer.http;

import cn.hutool.core.util.ArrayUtil;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.RuntimeResp;
import hekr.me.iotos.softgateway.common.dto.TokenResp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    respList.addAll(getRespList("12e8ef8b5332fa2d"));
    respList.addAll(getRespList("35dd9a224718c971"));
    respList.addAll(getRespList("30524b183f8e3ae3"));
    respList.addAll(getRespList("145bdd7a3c27d2d5"));
    respList.addAll(getRespList("5daad14f8e7e88d4"));

    BaseResp<List<RuntimeResp>> listBaseResp = new BaseResp<>();
    List<RuntimeResp> result =
        respList.stream()
            .filter(runtimeResp -> ArrayUtil.contains(deviceIds, runtimeResp.getDeviceId()))
            .collect(Collectors.toList());
    listBaseResp.setData(result);
    return listBaseResp;
  }

  private List<RuntimeResp> getRespList(String deviceId) {
    List<RuntimeResp> respList = new ArrayList<>();

    int errorCode = Math.random() > 0.1 ? 1 : 0;
    DecimalFormat df = new DecimalFormat("#.00");
    for (int i = 101; i < 112; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDeviceId(deviceId);
      dev.setDefineIndex(i);
      dev.setValue(Double.parseDouble(df.format(1 + Math.random() * (900))));
      respList.add(dev);
    }
    for (int i = 203; i < 210; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDefineIndex(i);
      dev.setDeviceId(deviceId);
      dev.setValue(Double.parseDouble(df.format(1 + Math.random() * (900))));
      respList.add(dev);
    }

    for (int i = 210; i < 214; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDefineIndex(i);
      dev.setDeviceId(deviceId);
      dev.setValue(Math.random() > 0.1 ? 1 : 0);
      respList.add(dev);
    }

    return respList;
  }
}
