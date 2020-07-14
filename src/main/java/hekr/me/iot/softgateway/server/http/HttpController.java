package hekr.me.iot.softgateway.server.http;

import hekr.me.iot.softgateway.server.tcp.codec.DataCodec;
import hekr.me.iot.softgateway.server.tcp.codec.RawDataCodec;
import hekr.me.iot.softgateway.common.klink.DevSend;
import hekr.me.iot.softgateway.gateway.MqttServer;
import hekr.me.iot.softgateway.server.tcp.TcpPacket;
import hekr.me.iot.softgateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

@Slf4j
@RequestPath(value = "/gateway")
public class HttpController {
  private DataCodec dataCodec = new RawDataCodec();

  public HttpController() {}

  @RequestPath(value = "/push")
  public HttpResponse push(HttpRequest request) throws Exception {
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(request.getBody());
    DevSend klinkDev = dataCodec.decode(tcpPacket, null);
    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      throw new RuntimeException();
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    MqttServer.sendKlink(klinkDev);

    HttpResponse ret = Resps.bytes(request, TcpPacket.HTTP_RESP, "ok");
    return ret;
  }

  @RequestPath(value = "/test")
  public HttpResponse sendCommand(HttpRequest request) throws Exception {
    HttpResponse ret = Resps.bytes(request, JsonUtil.toBytes("test ok"), "ok");
    return ret;
  }
}
