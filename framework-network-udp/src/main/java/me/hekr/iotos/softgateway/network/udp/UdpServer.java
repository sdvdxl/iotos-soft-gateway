package me.hekr.iotos.softgateway.network.udp;

import java.net.InetSocketAddress;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.ConnectionContext;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
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
public class UdpServer<T> extends UdpClient<T> {

  public UdpServer(String host, int port, int bindPort) {
    super(host, port, bindPort);
  }

  public UdpServer(int bindPort) {
    super(null, 0, bindPort);
  }

  @Override
  public void setPacketCoder(PacketCoder<T> packetCoder) {
    this.packetCoderHandlerFactory = () -> new UdpCodecHandler<>(packetCoder);
  }

  /**
   * 发送数据
   *
   * @param host 要发送的 host
   * @param port 要发送的端口
   * @param t 要发送的内容
   * @return response
   */
  @SneakyThrows
  public T send(String host, int port, T t) {
    InternalPacket<T> internalPacket = InternalPacket.wrap(t, new InetSocketAddress(host, port));
    return doSend(internalPacket);
  }

  @SneakyThrows
  public T send(ConnectionContext<T> ctx, T t) {
    return send(ctx.getAddress(), t);
  }

  @SneakyThrows
  public T send(InetSocketAddress address, T t) {
    InternalPacket<T> internalPacket = InternalPacket.wrap(t, address);
    return doSend(internalPacket);
  }
}
