package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.ChargeReq;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class TestController {

  @Autowired Random random;

  @RequestPath(value = "/GetCarCodes.action")
  public HttpResponse getCarCodes(HttpRequest request) throws Exception {
    EntranceReq entranceReq = JsonUtil.fromBytes(request.getBody(), EntranceReq.class);
    BaseResp baseResp = new BaseResp();
    List<EntranceReq> info = new ArrayList<>();
    EntranceReq car1 = new EntranceReq();
    car1.setCarCode(Integer.toString(random.nextInt(100000)));
    car1.setInTime(Integer.toString(random.nextInt(100000) + 1595330303));
    EntranceReq car2 = new EntranceReq();
    car2.setCarCode(Integer.toString(random.nextInt(100000)));
    car2.setInTime(Integer.toString(random.nextInt(100000) + 1595330303));

    if ("carCode001".equals(entranceReq.getCarCode())) {
      info.add(car1);
    } else if ("carCode002".equals(entranceReq.getCarCode())) {
      info.add(car2);
    } else {
      info.add(car1);
      info.add(car2);
    }
    baseResp.setResMsg(JsonUtil.toJson(info));
    return Resps.bytes(request, JsonUtil.toBytes(baseResp), "ok");
  }

  @RequestPath(value = "/GetCarInfo")
  public HttpResponse getCarInfo(HttpRequest request) throws Exception {
    ChargeReq chargeReq = JsonUtil.fromBytes(request.getBody(), ChargeReq.class);
    BaseResp baseResp = new BaseResp();
    List<ChargeReq> info = new ArrayList<>();
    ChargeReq car1 = new ChargeReq();
    car1.setCarCode(Integer.toString(random.nextInt(100000)));
    car1.setInTime(Integer.toString(random.nextInt(100000) + 1595330303));
    int chargeMoney1 = random.nextInt(50);
    car1.setChargeMoney(chargeMoney1);
    car1.setPaidMoney(chargeMoney1);
    car1.setJMMoney(0);

    ChargeReq car2 = new ChargeReq();
    car1.setCarCode(Integer.toString(random.nextInt(100000)));
    car1.setInTime(Integer.toString(random.nextInt(100000) + 1595330303));
    int chargeMoney2 = random.nextInt(50);
    car1.setChargeMoney(chargeMoney2);
    car1.setPaidMoney(chargeMoney2);
    car1.setJMMoney(0);
    if ("carCode001".equals(chargeReq.getCarCode())) {
      info.add(car1);
    } else if ("carCode002".equals(chargeReq.getCarCode())) {
      info.add(car2);
    } else {
      info.add(car1);
      info.add(car2);
    }

    baseResp.setResMsg(JsonUtil.toJson(info));
    return Resps.bytes(request, JsonUtil.toBytes(baseResp), "ok");
  }

//  @RequestPath(value = "/GetDiscountMoney")
//  public HttpResponse getDiscountMoney(HttpRequest request) throws Exception {
//
//  }
}
