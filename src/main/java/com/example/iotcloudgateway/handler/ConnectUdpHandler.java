package com.example.iotcloudgateway.handler;

import com.example.iotcloudgateway.subMqtt.MqttServer;
import com.example.iotcloudgateway.dto.SubKlinkAction;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.tio.core.Node;
import org.tio.core.udp.UdpPacket;
import org.tio.core.udp.intf.UdpHandler;

import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ConnectUdpHandler implements UdpHandler {
  @Autowired private MqttServer mqttServer;

  @SneakyThrows
  @Override
  public void handler(UdpPacket udpPacket, DatagramSocket datagramSocket) {
    Node remote = udpPacket.getRemote();
    byte[] payload = udpPacket.getData();
    if (payload == null || payload.length == 0) {
      log.warn(
          "klink json decode fail from {}:{}, payload is empty", remote.getIp(), remote.getPort());
      return;
    }

    if (log.isDebugEnabled()) {
      log.debug(
          "remote ip:{} ,port:{}, payload: {}",
          remote.getIp(),
          remote.getPort(),
          new String(payload, StandardCharsets.UTF_8));
    }

    KlinkDev klinkDev;
    try {
      klinkDev = JsonUtil.fromBytes(payload, KlinkDev.class);
    } catch (Exception e) {
      log.warn(
          "klink json decode fail from {}:{}, payload: {}",
          remote.getIp(),
          remote.getPort(),
          new String(payload));
      return;
    }

    switch (klinkDev.getAction()) {
      case SubKlinkAction.ADD_TOPO:
        mqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_LOGIN:
        mqttServer.devLogin(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_LOGOUT:
        mqttServer.devLogout(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.GET_TOPO:
        mqttServer.devTopo();
        break;
      case SubKlinkAction.DEL_TOPO:
        mqttServer.delDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_SEND:
        mqttServer.devSend(JsonUtil.toJson(klinkDev));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + klinkDev.getAction());
    }
  }
}
