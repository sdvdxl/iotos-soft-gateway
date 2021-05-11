package iot.example.iotcloudgateway;

import me.hekr.iotos.softgateway.core.utils.ParseUtil;

//
// import hekr.me.iotos.softgateway.utils.SignUtil;
// import java.sql.Timestamp;
// import java.text.DecimalFormat;
// import java.text.SimpleDateFormat;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.time.ZoneOffset;
// import java.util.Date;
//
public class IotCloudGatewayApplicationTests {
  public static void main(String[] args) {

    byte HEAD = (byte) 0xf0;
    System.out.println(ParseUtil.byte2int(HEAD));
    //    //    BaseResp<List<TokenResp>> tokenRespBaseResp = new BaseResp<>();
    //    //    tokenRespBaseResp.setInfo("请求(或处理)成功");
    //    //    tokenRespBaseResp.setStatusCode(200);
    //    //    TokenResp tokenResp = new TokenResp();
    //    //    tokenResp.setApplicationId("test");
    //    //    tokenResp.setId("c9648f504179bdd2");
    //    //    tokenResp.setKey("b62987f93ced7dd2");
    //    //    List<TokenResp> list = new ArrayList<>();
    //    //    list.add(tokenResp);
    //    //    tokenRespBaseResp.setData(list);
    //    //
    //    //    String s = JsonUtil.toJson(tokenRespBaseResp);
    //    //    BaseResp<List<TokenResp>> o = JsonUtil.fromJson(s, new
    //    // TypeReference<BaseResp<List<TokenResp>>>() {});
    //    //    System.out.println(o);
    //
    ////    System.out.println(
    ////        SignUtil.getSignature(
    ////            "1596420104672",
    ////            "1006770035",
    ////            "fbe41c01b33f454a",
    ////
    // "energyType=01&parentType=Device&parentId=D00000002&beginDate=2018-03-01&endDate=2018-03-02&dateRange=Day"));
    //    System.out.println(getTotalEnergy(1));
    //    System.out.println(getTotalEnergy(1));
    //    System.out.println(getTotalEnergy(1));
    //    System.out.println(getTotalEnergy(1));
    //
    //    System.out.println(getTotalEnergy(1));
    //
    //    LocalDateTime now = LocalDateTime.now();
    //    LocalDateTime startDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
    //    LocalDateTime endDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
    //    long l = startDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    //    Date date = new Date(l);
    //    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    //    String startTime = format.format(date);
    //    System.out.println(startTime);
    ////    Date date = format.parse(str2);//格式化获取到的日期，
    //  }
    //
    //  private static double getTotalEnergy(double fix) {
    //    DecimalFormat df = new DecimalFormat("#.0000");
    //    double value = ((double) System.currentTimeMillis() / 100000 - 15900000.0) * fix;
    //    return Double.parseDouble(df.format(value));
  }
  //
  //
}
