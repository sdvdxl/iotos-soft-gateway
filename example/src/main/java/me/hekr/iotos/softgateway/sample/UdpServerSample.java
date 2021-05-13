package me.hekr.iotos.softgateway.sample;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;
import me.hekr.iotos.softgateway.network.udp.UdpServer;

/**
 * udp 服务端
 *
 * @author iotos
 */
public class UdpServerSample {
  static final int BIND_PORT = 1023;
  static final PacketCoder<String> PACKET_CODER =
      new PacketCoder<String>() {
        @Override
        public byte[] encode(String s) {
          return s.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public DecodePacket decode(byte[] bytes) {
          return DecodePacket.wrap(new String(bytes, StandardCharsets.UTF_8), bytes.length);
        }
      };

  public static void main(String[] args) throws InterruptedException {
    UdpServer<String> server = new UdpServer<>(BIND_PORT);
    server.setPacketCoder(PACKET_CODER);
    server.setMessageListener(
        ctx -> {
          System.out.println("udp server 收到：" + ctx.getMessage());
          server.send(ctx, "server 回复收到：" + ctx.getMessage());
        });
    server.start();
    new CountDownLatch(1).await();
  }
}
