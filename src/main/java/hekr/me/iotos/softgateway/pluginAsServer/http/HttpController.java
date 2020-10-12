package hekr.me.iotos.softgateway.pluginAsServer.http;

import cn.hutool.core.util.ArrayUtil;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.EnergyMeterReq;
import hekr.me.iotos.softgateway.common.dto.EnergyMeterResp;
import hekr.me.iotos.softgateway.common.dto.EnergyStatDataReq;
import hekr.me.iotos.softgateway.common.dto.EnergyStatDataResp;
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

}
