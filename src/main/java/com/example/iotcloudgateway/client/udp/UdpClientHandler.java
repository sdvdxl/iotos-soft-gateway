package com.example.iotcloudgateway.client.udp;

import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.klink.KlinkDev;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.Node;
import org.tio.core.udp.UdpPacket;
import org.tio.core.udp.intf.UdpHandler;

import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class UdpClientHandler implements UdpHandler {
  @Override
  @SneakyThrows
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
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_LOGIN:
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
      default:
        throw new IllegalStateException("Unexpected value: " + klinkDev.getAction());
    }
  }
}
