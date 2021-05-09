package me.hekr.iotos.softgateway.sample;

import java.nio.charset.Charset;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
import me.hekr.iotos.softgateway.network.udp.UdpClient;

/**
 * udp 客户端
 *
 * @author iotos
 */
public class UdpClientSample {
  private static final PacketCoder<String> PACKET_CODER =
      new PacketCoder<String>() {
        @Override
        public byte[] encode(String s) {
          return s.getBytes();
        }

        @Override
        public DecodePacket decode(byte[] bytes) {
          return DecodePacket.wrap(new String(bytes, Charset.forName("GBK")), bytes.length);
        }
      };

  public static void main(String[] args) {
    UdpClient<String> client = new UdpClient<>("localhost", 1024, 1021);
    client.setMessageListener(
        ctx -> {
          System.out.println("收到来自 " + ctx.getAddress() + " 的消息：" + ctx.getMessage());
        });
    client.setPacketCoder(PACKET_CODER);
    client.start();
    client.send("hello");
  }
}
