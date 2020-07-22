package hekr.me.iotos.softgateway.pluginAsClient.http;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.ChargeReq;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
//@Service
@Component
public class TestClient {
    @Autowired
    ProxyConfig proxyConfig;

    private Random random=new Random();

    public void inOutRecord(HttpRequest request) throws Exception {
        List<EntranceReq> info = new ArrayList<>();
        EntranceReq car = new EntranceReq();

        car.setCarCode(generateCarID());
        car.setInTime(generateTime()[0]);
        car.setPassTime(generateTime()[1]);
        car.setParkID(Integer.toString(random.nextInt(10)));
        car.setInOrOut(Integer.toString(random.nextInt(1)));
        car.setGUID(Integer.toString(random.nextInt(1000000)));
        car.setChannelID(Integer.toString(random.nextInt(10)));
        car.setChannelName("test");
        car.setImagePath("https://www.test.com/libinhong/p/10988752.png");
        info.add(car);
        HttpUtils httpUtils = new HttpUtils();
        String url = proxyConfig.getHTTP_URL() + "/gateway/postInOutRecord";
//        httpUtils.post()

    }

    @RequestPath(value = "/postChargeRecord")
    public HttpResponse chargeRecord(HttpRequest request) throws Exception {
        BaseResp baseResp = new BaseResp();
        List<ChargeReq> info = new ArrayList<>();
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

        info.add(charge);

        baseResp.setResMsg(JsonUtil.toJson(info));
        return Resps.bytes(request, JsonUtil.toBytes(baseResp), "ok");
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
