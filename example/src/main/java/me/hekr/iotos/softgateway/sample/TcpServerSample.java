package me.hekr.iotos.softgateway.sample;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;
import me.hekr.iotos.softgateway.network.tcp.listener.TcpMessageListener;
import me.hekr.iotos.softgateway.network.tcp.TcpServer;
import me.hekr.iotos.softgateway.network.tcp.TcpServerPacketContext;

/**
 * tcp 服务端
 *
 * @author iotos
 */
public class TcpServerSample {
  public static void main(String[] args) throws InterruptedException {
    TcpServer<String> server = new TcpServer<>();
    server.bind(1024);
    server.setMessageListener(new MyMessageListener(server));
    server.setPackageCoder(new MyPacketCoder());
    server.setHeartbeatTimeout(3000);
    server.start();
    new CountDownLatch(1).await();
    //  server.close()
  }

  static class MyPacketCoder implements PacketCoder<String> {
    @Override
    public byte[] encode(String s) {
      return s.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public DecodePacket decode(byte[] bytes) {
      return DecodePacket.wrap(new String(bytes), bytes.length);
    }
    ;
  }

  static class MyMessageListener implements TcpMessageListener<String> {

    private final TcpServer<String> server;

    public MyMessageListener(TcpServer<String> server) {
      this.server = server;
    }

    @Override
    public void onMessage(TcpServerPacketContext<String> ctx) {
      System.out.println("收到来自: " + ctx.getAddress() + " 的消息，" + ctx.getMessage());
      server.writeAndFlush(ctx, "回复:" + ctx.getMessage());
    }
  }
}
