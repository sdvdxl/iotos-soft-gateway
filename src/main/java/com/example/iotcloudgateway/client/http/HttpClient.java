package com.example.iotcloudgateway.client.http;

import com.example.iotcloudgateway.codec.DataCodec;
import com.example.iotcloudgateway.codec.RawDataCodec;
import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.klink.DevSend;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.server.tcp.TcpPacket;
import com.example.iotcloudgateway.utils.JsonUtil;
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
    Response response = httpUtils.get(url, null, null);
    TcpPacket tcpPacket = new TcpPacket();
    assert response.body() != null;
    tcpPacket.setBody(response.body().bytes());
    DevSend klinkDev = dataCodec.decode(tcpPacket, null);

    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      return;
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    switch (klinkDev.getAction()) {
      case SubKlinkAction.ADD_TOPO:
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getDevSecret());
        break;
      case SubKlinkAction.DEV_LOGIN:
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getDevSecret());
        MqttServer.devLogin(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_LOGOUT:
        MqttServer.devLogout(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.GET_TOPO:
        MqttServer.devTopo();
        break;
      case SubKlinkAction.DEL_TOPO:
        MqttServer.delDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_SEND:
        MqttServer.devSend(JsonUtil.toJson(klinkDev));
        break;
      case SubKlinkAction.HEARTBEAT:
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + klinkDev.getAction());
    }
  }
}
