package me.hekr.iotos.softgateway.pluginAsServer.tcp;

import lombok.SneakyThrows;
import me.hekr.iotos.softgateway.common.config.IotOsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;

/** tcp server starter 用于配置tcp服务端相关配置，并可被调用以启动服务端程序 */
@Service
public class TcpServerStarter {
  @Autowired TcpServerMsgHandler tcpServerMsgHandler;
  @Autowired TcpServerListener tcpServerListener;
  @Autowired
  IotOsConfig iotOsConfig;

  @SneakyThrows
  //  @PostConstruct
  public void start() {
    ServerTioConfig serverTioConfig = new ServerTioConfig(tcpServerMsgHandler, tcpServerListener);
    TioServer tioServer = new TioServer(serverTioConfig); // 可以为空
    tioServer.start(null, iotOsConfig.getTcpServerPort());
  }
}
