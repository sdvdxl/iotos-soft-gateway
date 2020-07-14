package hekr.me.iot.softgateway.client.http;

import hekr.me.iot.softgateway.common.klink.DevSend;
import hekr.me.iot.softgateway.gateway.MqttServer;
import hekr.me.iot.softgateway.server.tcp.TcpPacket;
import hekr.me.iot.softgateway.server.tcp.codec.DataCodec;
import hekr.me.iot.softgateway.server.tcp.codec.RawDataCodec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** */
@Slf4j
public class HttpClient {

  private DataCodec dataCodec = new RawDataCodec();

  @SneakyThrows
  public void getAndSend() {
    // 此处填写访问的路径
    String url = "/model/protocol/";
    HttpUtils httpUtils = new HttpUtils();
    byte[] response = httpUtils.get(url, null, null);
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(response);
    DevSend klinkDev = dataCodec.decode(tcpPacket, null);

    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      return;
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    MqttServer.sendKlink(klinkDev);
  }
}
