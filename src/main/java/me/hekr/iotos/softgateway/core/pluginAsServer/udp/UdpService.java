package me.hekr.iotos.softgateway.core.pluginAsServer.udp;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.tio.core.udp.UdpServer;
import org.tio.core.udp.UdpServerConf;
import org.tio.utils.jfinal.P;

/**
 * @author iotos
 * @date 2020/7/13
 */
@Service
public class UdpService {
  public static UdpServer udpServer;

  @SneakyThrows
  public static void init() {
    ConnectUdpHandler connectUdpHandler = new ConnectUdpHandler();
    Integer port = P.getInt("udp.server.port");
    Integer timeout = P.getInt("udp.server.timeout");
    UdpServerConf udpServerConf = new UdpServerConf(port, connectUdpHandler, timeout);
    udpServer = new UdpServer(udpServerConf);
    udpServer.start();
  }
}
