package com.example.iotcloudgateway.server.http;

import com.example.iotcloudgateway.codec.DataCodec;
import com.example.iotcloudgateway.codec.RawDataCodec;
import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.klink.DevSend;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.server.tcp.TcpPacket;
import com.example.iotcloudgateway.utils.JsonUtil;
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
}
