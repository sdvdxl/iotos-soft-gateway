package me.hekr.iotos.softgateway.network.udp;

import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.InetSocketAddress;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.client.AbstractClient;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;

/**
 * UDP client
 *
 * <p>支持异步和同步模式
 *
 * <p>同步模式不需要设置 MessageListener；要求一问一答模式，即发送请求，回复请求，可以配合超时时间，默认2s
 *
 * <p>异步模式需要设置 MessageListener，不严格要求是一问一答模式，有消息收到就会进入回调函数
 *
 * @author iotos
 */
@Slf4j
public class UdpClient<T> extends AbstractClient<T> {

  public UdpClient(String host, int port, int bindPort) {
    super(NioDatagramChannel.class, 2048);
    this.host = host;
    this.port = port;
    this.bindPort = bindPort;
  }

  public UdpClient(String host, int port, int bindPort, int maxDatagramSize) {
    super(NioDatagramChannel.class, maxDatagramSize);
    this.host = host;
    this.port = port;
    this.bindPort = bindPort;
  }

  @Override
  public void setPacketCoder(PacketCoder<T> packetCoder) {
    packetCoderHandler = new UdpCodecHandler<>(packetCoder);
  }

  /**
   * 发送广播
   *
   * @param t message
   * @param port 端口
   * @return 回复消息
   */
  @SneakyThrows
  public T sendBroadcast(T t, int port) {
    return doSend(InternalPacket.wrap(t, new InetSocketAddress("255.255.255.255", port)));
  }
}
