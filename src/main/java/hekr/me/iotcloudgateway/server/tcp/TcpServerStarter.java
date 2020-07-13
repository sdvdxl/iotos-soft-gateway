package hekr.me.iotcloudgateway.server.tcp;

import lombok.SneakyThrows;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.utils.jfinal.P;

/** tcp server starter 用于配置tcp服务端相关配置，并可被调用以启动服务端程序 */
public class TcpServerStarter {
  static ServerAioHandler aioHandler = new TcpServerMsgHandler();
  static ServerAioListener aioListener = new TcpServerListener();
  static ServerTioConfig serverTioConfig = new ServerTioConfig(aioHandler, aioListener);
  public static TioServer tioServer = new TioServer(serverTioConfig); // 可以为空

  static String serverIp = null;
  static int serverPort = P.getInt("tcp.server.port");

  @SneakyThrows
  public static void start() {
    tioServer.start(serverIp, serverPort);
  }
}
