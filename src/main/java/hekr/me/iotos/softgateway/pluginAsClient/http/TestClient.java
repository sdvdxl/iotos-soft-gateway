package hekr.me.iotos.softgateway.pluginAsClient.http;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.dto.ChargeReq;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.utils.MapUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class TestClient {
  @Autowired ProxyConfig proxyConfig;

  private Random random = new Random();

  @Scheduled(fixedDelay = 10 * 1000)
  public void inOutRecord() throws Exception {
    EntranceReq car = new EntranceReq();

    car.setCarCode(generateCarID());
    car.setInTime(generateTime()[0]);
    car.setPassTime(generateTime()[1]);
    car.setParkID(Integer.toString(random.nextInt(10)));
    car.setInOrOut(Integer.toString(random.nextInt(1)));
    car.setGUID(Integer.toString(random.nextInt(100)));
    car.setChannelID("1");
    car.setChannelName("test");
    car.setImagePath("https://www.test.com/libinhong/p/10988752.png");

    Map<String, String> headerParams = new HashMap<>();
    headerParams.put("Content-Type", "application/json");
    Map<String, Object> body = MapUtil.objectToMap(car);
    HttpUtils httpUtils = new HttpUtils();
    String url = proxyConfig.getHTTP_URL() + "/gateway/postInOutRecord";
    httpUtils.post(url, headerParams, body);
  }

//  @Scheduled(fixedDelay = 10 * 1000)
  public void chargeRecord() throws Exception {
    ChargeReq charge = new ChargeReq();
    int chargeMoney = random.nextInt(50);
    charge.setRecordID(Integer.toString(random.nextInt(10000)));
    charge.setCarCode(generateCarID());
    charge.setInTime(generateTime()[0]);
    charge.setPayTime(generateTime()[1]);
    charge.setParkID(Integer.toString(random.nextInt(10000)));
    charge.setChargeMoney(chargeMoney);
    charge.setGUID(Integer.toString(random.nextInt(1000000)));
    charge.setPaidMoney(chargeMoney);
    charge.setJMMoney(0);
    charge.setChargeType(random.nextInt(4));
    charge.setChargeSource(random.nextInt(15));
    charge.setAmountType(random.nextInt(4));

    Map<String, String> headerParams = new HashMap<>();
    headerParams.put("Content-Type", "application/json");
    Map<String, Object> body = MapUtil.objectToMap(charge);
    HttpUtils httpUtils = new HttpUtils();
    String url = proxyConfig.getHTTP_URL() + "/gateway/postChargeRecord";
    httpUtils.post(url, headerParams, body);
  }

  // 车牌号的组成一般为：省份+地区代码+5位数字/字母
  private static String generateCarID() {

    char[] provinceAbbr = { // 省份简称 4+22+5+3
      '京', '津', '沪', '渝', '冀', '豫', '云', '辽', '黑', '湘', '皖', '鲁', '苏', '浙', '赣', '鄂', '甘', '晋', '陕',
      '吉', '闽', '贵', '粤', '青', '川', '琼', '宁', '新', '藏', '桂', '蒙', '港', '澳', '台'
    };
    String alphas = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890"; // 26个字母 + 10个数字

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
