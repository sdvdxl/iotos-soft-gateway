package me.hekr.iotos.softgateway.example.pluginAsServer.udp;

import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.klink.KlinkDev;
import me.hekr.iotos.softgateway.core.utils.JsonUtil;
import org.tio.core.Node;
import org.tio.core.udp.UdpPacket;
import org.tio.core.udp.intf.UdpHandler;

@Slf4j
public class ConnectUdpHandler implements UdpHandler {

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

    //    ProxyService.sendKlink(klinkDev);
  }
}
