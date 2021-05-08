package me.hekr.iotos.softgateway.sample;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.MessageListener;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
import me.hekr.iotos.softgateway.network.tcp.TcpServer;

/**
 * tcp 服务端
 *
 * @author iotos
 */
public class TcpServerSample {
  public static void main(String[] args) throws InterruptedException {
    TcpServer<String> server = new TcpServer<>();
    server.bind(1024);
    server.setMessageListener(messageListener());
    server.setPackageCoder(packetCoder());
    server.setHeartbeatTimeout(3000);
    server.start();
    new CountDownLatch(1).await();
    //  server.close()
  }

  private static PacketCoder<String> packetCoder() {
    return new PacketCoder<String>() {
      @Override
      public byte[] encode(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
      }

      @Override
      public DecodePacket decode(byte[] bytes) {
        return DecodePacket.wrap(new String(bytes), bytes.length);
      }
    };
  }

  private static MessageListener<String> messageListener() {
    return ctx -> {
      System.out.println("收到来自: " + ctx.getAddress() + " 的消息，" + ctx.getMessage());
      ctx.writeAndFlush("回复:" + ctx.getMessage());
    };
  }
}
