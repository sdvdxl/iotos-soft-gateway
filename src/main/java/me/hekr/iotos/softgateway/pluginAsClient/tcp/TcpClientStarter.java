package me.hekr.iotos.softgateway.pluginAsClient.tcp;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.config.ProxyConfig;
import me.hekr.iotos.softgateway.pluginAsClient.tcp.packet.TcpPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

@Slf4j
@Service
public class TcpClientStarter {
  @Autowired private ProxyConfig proxyConfig;

  @Autowired private ClientAioHandler tioClientHandler;
  @Autowired private ClientAioListener aioListener;

  private TioClient tioClient;

  private ClientChannelContext clientChannelContext;

  //  @PostConstruct
  public void start() throws Exception {
    ReconnConf reconnConf = new ReconnConf(5000L);
    tioClient = new TioClient(new ClientTioConfig(tioClientHandler, aioListener, reconnConf));
    clientChannelContext =
        tioClient.connect(
            new Node(proxyConfig.getTCP_CONNECT_IP(), proxyConfig.getTCP_CONNECT_PORT()));
  }

  public void send(byte[] body) {
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(body);
    Tio.send(clientChannelContext, tcpPacket);
  }
}
