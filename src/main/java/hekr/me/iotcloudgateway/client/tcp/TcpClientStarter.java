package hekr.me.iotcloudgateway.client.tcp;

import hekr.me.iotcloudgateway.server.tcp.TcpPacket;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.utils.jfinal.P;

public class TcpClientStarter {
  static String serverIp = P.get("tcp.client.ip");
  static int serverPort = P.getInt("tcp.client.port");

  private static Node serverNode = new Node(serverIp, serverPort);

  // 用来自动连接的，不想自动连接请设为null
  private static ReconnConf reconnConf = new ReconnConf(5000L);

  private static ClientAioHandler tioClientHandler = new TcpClientHandler();
  private static ClientAioListener aioListener = new TcpClientListener();
  private static ClientTioConfig clientTioConfig =
      new ClientTioConfig(tioClientHandler, aioListener, reconnConf);

  private static TioClient tioClient = null;

  static ClientChannelContext clientChannelContext;

  public static void start() throws Exception {
    tioClient = new TioClient(clientTioConfig);
    clientChannelContext = tioClient.connect(serverNode);
    getMsg();
  }

  public static void getMsg() {
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(TcpPacket.GET_MSG);
    Tio.send(clientChannelContext, tcpPacket);
  }
}
