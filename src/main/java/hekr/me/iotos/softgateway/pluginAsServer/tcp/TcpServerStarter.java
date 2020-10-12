package hekr.me.iotos.softgateway.pluginAsServer.tcp;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.utils.jfinal.P;

/** tcp server starter 用于配置tcp服务端相关配置，并可被调用以启动服务端程序 */
@Service
public class TcpServerStarter {
  @Autowired TcpServerMsgHandler tcpServerMsgHandler;
  @Autowired TcpServerListener tcpServerListener;
  @Autowired ProxyConfig proxyConfig;

  @SneakyThrows
  //  @PostConstruct
  public void start() {
    ServerTioConfig serverTioConfig = new ServerTioConfig(tcpServerMsgHandler, tcpServerListener);
    TioServer tioServer = new TioServer(serverTioConfig); // 可以为空
    tioServer.start(null, proxyConfig.getTCP_SERVER_PORT());
  }
}
