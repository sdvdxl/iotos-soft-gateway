package me.hekr.iotos.softgateway.network.tcp;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.client.AbstractClient;
import me.hekr.iotos.softgateway.network.common.PacketCoder;

/**
 * UDP client
 *
 * <p>支持异步和同步模式
 *
 * <p>同步模式不需要设置 UdpMessageListener；要求一问一答模式，即发送请求，回复请求，可以配合超时时间，默认2s
 *
 * <p>异步模式需要设置 UdpMessageListener，不严格要求是一问一答模式，有消息收到就会进入回调函数
 *
 * @author iotos
 */
@Slf4j
public class TcpClient<T> extends AbstractClient<T> {

  public TcpClient(String host, int port) {
    super(NioSocketChannel.class);
    this.host = host;
    this.port = port;
  }

  @Override
  public void setPacketCoder(PacketCoder<T> packetCoder) {
    packetCoderHandler = new TcpCodecHandler<>(packetCoder);
  }
}
