package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.ChargeReq;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.common.dto.GateControlReq;
import hekr.me.iotos.softgateway.utils.AESUtils;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.SneakyThrows;
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
@RequestPath
public class TestController {
  public TestController() {}

  @SneakyThrows
  @RequestPath(value = "/GetChannelInfo")
  public HttpResponse getChannelInfo(HttpRequest request) {
    //     将数据解码后
    GateControlReq gateControlReq =
        JsonUtil.fromBytes(
            AESUtils.decodeRequestData(new String(request.getBody())), GateControlReq.class);
    //    GateControlReq gateControlReq = JsonUtil.fromBytes(request.getBody(),
    // GateControlReq.class);
    BaseResp baseResp = new BaseResp();
    baseResp.setResMsg("通道口：" + gateControlReq.getChannelID() + " 操作成功");
    baseResp.setResCode(0);
    return Resps.json(request, baseResp);
  }
}
