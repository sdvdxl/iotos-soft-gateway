package hekr.me.iotos.softgateway.pluginAsClient.tcp;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.pluginAsClient.tcp.packet.TcpPacket;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.utils.jfinal.P;

@Slf4j
@Component
public class TcpClientStarter {
  @Autowired private ProxyConfig proxyConfig;

  @Autowired private ClientAioHandler tioClientHandler;
  @Autowired private ClientAioListener aioListener;

  private TioClient tioClient;

  ClientChannelContext clientChannelContext;

  @PostConstruct
  public void start() throws Exception {
    ReconnConf reconnConf = new ReconnConf(5000L);
    tioClient = new TioClient(new ClientTioConfig(tioClientHandler, aioListener, reconnConf));
    clientChannelContext =
        tioClient.connect(
            new Node(proxyConfig.getTCP_CONNECT_IP(), proxyConfig.getTCP_CONNECT_PORT()));
    getMsg();
  }

  public void getMsg() {
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(TcpPacket.GET_MSG);
    Tio.send(clientChannelContext, tcpPacket);
  }
}
