package com.example.iotcloudgateway.client.udp;

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

    MqttServer.sendKlink(klinkDev);
  }
}