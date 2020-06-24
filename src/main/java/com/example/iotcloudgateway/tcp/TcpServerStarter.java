package com.example.iotcloudgateway.tcp;

import lombok.SneakyThrows;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

/**
 * @author jiatao
 * @date 2020/6/24
 */
public class TcpServerStarter {
  static ServerAioHandler aioHandler = new TcpServerMsgHandler();
  static ServerAioListener aioListener = new TcpServerListener();
  static ServerTioConfig serverTioConfig = new ServerTioConfig(aioHandler, aioListener);
  public static TioServer tioServer = new TioServer(serverTioConfig); // 可以为空

  static String serverIp = null;
  static int serverPort = 7000;

  @SneakyThrows
  public static void start() {
    tioServer.start(serverIp, serverPort);
  }
}
