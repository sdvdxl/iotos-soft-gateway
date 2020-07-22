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
@RequestPath(value = "/gateway")
public class TestController {

  @Autowired Random random;

  @RequestPath(value = "/postInOutRecord")
  public HttpResponse inOutRecord(HttpRequest request) throws Exception {
    BaseResp baseResp = new BaseResp();
    List<EntranceReq> info = new ArrayList<>();
    EntranceReq car = new EntranceReq();

    car.setCarCode(generateCarID());
    car.setInTime(generateTime()[0]);
    car.setPassTime(generateTime()[1]);
    car.setParkID(Integer.toString(random.nextInt(10000)));
    car.setInOrOut(Integer.toString(random.nextInt(1)));
    car.setGUID(Integer.toString(random.nextInt(1000000)));
    car.setChannelID(Integer.toString(random.nextInt(10)));
    car.setChannelName("test");
    car.setImagePath("https://www.cnblogs.com/libinhong/p/10988752.png");

    info.add(car);

    baseResp.setResMsg(JsonUtil.toJson(info));
    return Resps.bytes(request, JsonUtil.toBytes(baseResp), "ok");
  }
//
//  @RequestPath(value = "/GetCarInfo")
//  public HttpResponse getCarInfo(HttpRequest request) throws Exception {
//    ChargeReq chargeReq = JsonUtil.fromBytes(request.getBody(), ChargeReq.class);
//    BaseResp baseResp = new BaseResp();
//    List<ChargeReq> info = new ArrayList<>();
//    ChargeReq car1 = new ChargeReq();
//    car1.setCarCode(Integer.toString(random.nextInt(100000)));
//    car1.setInTime(Integer.toString(random.nextInt(100000) + 1595330303));
//    int chargeMoney1 = random.nextInt(50);
//    car1.setChargeMoney(chargeMoney1);
//    car1.setPaidMoney(chargeMoney1);
//    car1.setJMMoney(0);
//
//    ChargeReq car2 = new ChargeReq();
//    car1.setCarCode(Integer.toString(random.nextInt(100000)));
//    car1.setInTime(Integer.toString(random.nextInt(100000) + 1595330303));
//    int chargeMoney2 = random.nextInt(50);
//    car1.setChargeMoney(chargeMoney2);
//    car1.setPaidMoney(chargeMoney2);
//    car1.setJMMoney(0);
//    if ("carCode001".equals(chargeReq.getCarCode())) {
//      info.add(car1);
//    } else if ("carCode002".equals(chargeReq.getCarCode())) {
//      info.add(car2);
//    } else {
//      info.add(car1);
//      info.add(car2);
//    }
//
//    baseResp.setResMsg(JsonUtil.toJson(info));
//    return Resps.bytes(request, JsonUtil.toBytes(baseResp), "ok");
//  }

  // 车牌号的组成一般为：省份+地区代码+5位数字/字母
  private static String generateCarID() {

    char[] provinceAbbr = { // 省份简称 4+22+5+3
      '京', '津', '沪', '渝', '冀', '豫', '云', '辽', '黑', '湘', '皖', '鲁', '苏', '浙', '赣', '鄂', '甘', '晋', '陕',
      '吉', '闽', '贵', '粤', '青', '川', '琼', '宁', '新', '藏', '桂', '蒙', '港', '澳', '台'
    };
    String alphas = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890"; // 24个字母 + 10个数字

    Random random = new Random(); // 随机数生成器

    String carID = "";

    // 省份+地区代码+·  如 湘A· 这个点其实是个传感器，不过加上美观一些
    carID += provinceAbbr[random.nextInt(34)]; // 注意：分开加，因为加的是2个char
    carID += alphas.charAt(random.nextInt(26)) + "·";

    // 5位数字/字母
    for (int i = 0; i < 5; i++) {
      carID += alphas.charAt(random.nextInt(36));
    }
    return carID;
  }

  private String[] generateTime() {
    String[] inOut = new String[2];
    String month = Integer.toString(random.nextInt(5) + 1);
    String day = Integer.toString(random.nextInt(29) + 1);

    int hour = random.nextInt(23);
    int outHour = hour + 1;
    String min = Integer.toString(random.nextInt(60));
    String sec = Integer.toString(random.nextInt(60));

    inOut[0] = "2020-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
    inOut[1] = "2020-" + month + "-" + day + " " + outHour + ":" + min + ":" + sec;
    return inOut;
  }
}
