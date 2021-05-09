package me.hekr.iotos.softgateway.sample;

import cn.hutool.core.thread.ThreadUtil;
import java.nio.charset.StandardCharsets;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
import me.hekr.iotos.softgateway.network.common.client.EventListenerAdapter;
import me.hekr.iotos.softgateway.network.tcp.TcpClient;

/**
 * tcp 客户端，
 *
 * @author iotos
 */
public class TcpClientSample {
  static PacketCoder<String> packetCoder =
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

  public static void main(String[] args) {
    TcpClient<String> client = new TcpClient<>("localhost", 1024);
    client.setEventListener(new EventListenerAdapter<>());
    client.setPacketCoder(packetCoder);
    client.setMessageListener(
        ctx -> System.out.println("收到来自：" + ctx.getAddress() + " 的消息：" + ctx.getMessage()));
    client.setTimeout(30000);
    client.start();
    while (true) {
      client.send("hello");
      ThreadUtil.sleep(1000);
    }
  }
}
