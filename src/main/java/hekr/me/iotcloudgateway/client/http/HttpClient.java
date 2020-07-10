package hekr.me.iotcloudgateway.client.http;

import hekr.me.iotcloudgateway.codec.DataCodec;
import hekr.me.iotcloudgateway.codec.RawDataCodec;
import hekr.me.iotcloudgateway.klink.DevSend;
import hekr.me.iotcloudgateway.mqtt.MqttServer;
import hekr.me.iotcloudgateway.server.tcp.TcpPacket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

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
