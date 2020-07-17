package hekr.me.iotos.softgateway.pluginAsClient.udp;

import org.tio.core.udp.UdpClient;
import org.tio.core.udp.UdpClientConf;
import org.tio.utils.jfinal.P;

public class UdpClientStarter {
  public static final byte[] GET_MSG = {1};
  private static final String ip = P.get("udp.client.ip");
  private static final Integer port = P.getInt("udp.client.port");
  private static final Integer timeout = P.getInt("udp.client.timeout");

  private static UdpClientConf udpClientConf = new UdpClientConf(ip, port, timeout);

  public static void start() {
    UdpClient udpClient = new UdpClient(udpClientConf);
    udpClient.start();
    udpClient.send(GET_MSG);
  }
}
