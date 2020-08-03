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

  @GetMapping("/GetRuntimeData")
  public BaseResp<List<RuntimeResp>> getRuntimeData(@RequestParam String[] deviceIds) {
    List<RuntimeResp> respList = new ArrayList<>();
    for (String deviceId : deviceIds) {
      respList.addAll(getRespList(deviceId));
    }

    BaseResp<List<RuntimeResp>> listBaseResp = new BaseResp<>();
    listBaseResp.setData(respList);
    return listBaseResp;
  }

  @GetMapping("/GetEnergyStatData")
  public BaseResp<List<EnergyStatDataResp>> getEnergyMeterData(@RequestParam String parentId) {
    BaseResp<List<EnergyStatDataResp>> listBaseResp = new BaseResp<>();
    listBaseResp.setData(getTotalResp(parentId));
    return listBaseResp;
  }

  private List<EnergyStatDataResp> getTotalResp(String devId) {
    List<EnergyStatDataResp> resps = new ArrayList<>();
    EnergyStatDataResp energyStatDataResp = new EnergyStatDataResp();
    energyStatDataResp.setDeviceId(devId);
    energyStatDataResp.setDefineIndex(111);
    energyStatDataResp.setCurrentAmount(getTotalEnergy());
    resps.add(energyStatDataResp);
    return resps;
  }

  private List<RuntimeResp> getRespList(String deviceId) {
    List<RuntimeResp> respList = new ArrayList<>();

    int errorCode = Math.random() > 0.1 ? 1 : 0;
    DecimalFormat df = new DecimalFormat("#.00");
    for (int i = 101; i < 104; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDeviceId(deviceId);
      dev.setDefineIndex(i);
      dev.setValue(Double.parseDouble(df.format(220 + Math.random() * (10))));
      respList.add(dev);
    }
    for (int i = 104; i < 107; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDeviceId(deviceId);
      dev.setDefineIndex(i);
      dev.setValue(Double.parseDouble(df.format(380 + Math.random() * (10))));
      respList.add(dev);
    }
    for (int i = 107; i < 110; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDeviceId(deviceId);
      dev.setDefineIndex(i);
      dev.setValue(Double.parseDouble(df.format(10 + Math.random() * (10))));
      respList.add(dev);
    }
    RuntimeResp dev1 = new RuntimeResp();
    dev1.setErrorCode(errorCode - 1);
    dev1.setDeviceId(deviceId);
    dev1.setDefineIndex(110);
    dev1.setValue(Double.parseDouble(df.format(3500 + Math.random() * (1000))));
    respList.add(dev1);

    RuntimeResp dev2 = new RuntimeResp();
    dev2.setErrorCode(errorCode - 1);
    dev2.setDeviceId(deviceId);
    dev2.setDefineIndex(111);
    dev2.setValue(Double.parseDouble(df.format(1 + Math.random() * (900))));
    respList.add(dev2);

    for (int i = 203; i < 205; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDefineIndex(i);
      dev.setDeviceId(deviceId);
      dev.setValue(Double.parseDouble(df.format(1500 + Math.random() * (500))));
      respList.add(dev);
    }

    // 视在功率
    RuntimeResp dev3 = new RuntimeResp();
    dev3.setErrorCode(errorCode - 1);
    dev3.setDeviceId(deviceId);
    dev3.setDefineIndex(205);
    dev3.setValue(Double.parseDouble(df.format(2500 + Math.random() * (2000))));
    respList.add(dev3);

    // 功率因数
    RuntimeResp dev4 = new RuntimeResp();
    dev4.setErrorCode(errorCode - 1);
    dev4.setDeviceId(deviceId);
    dev4.setDefineIndex(206);
    dev4.setValue(Double.parseDouble(df.format(0.85 + Math.random() * (0.1))));
    respList.add(dev4);

    for (int i = 207; i < 210; i++) {
      RuntimeResp dev = new RuntimeResp();
      dev.setErrorCode(errorCode - 1);
      dev.setDefineIndex(i);
      dev.setDeviceId(deviceId);
      dev.setValue(Double.parseDouble(df.format(0 + Math.random() * (100))));
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

  /** 模拟总电能 */
  private double getTotalEnergy() {
    DecimalFormat df = new DecimalFormat("#.00");

    double value = ((double) System.currentTimeMillis() / 100000 - 15900000.0);

    return Double.parseDouble(df.format(value));
  }
}
