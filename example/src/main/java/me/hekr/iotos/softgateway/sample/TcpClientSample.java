package me.hekr.iotos.softgateway.sample;

import cn.hutool.core.thread.ThreadUtil;
import java.nio.charset.StandardCharsets;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
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
          return s.getBytes();
        }

        @Override
        public DecodePacket decode(byte[] bytes) {
          return DecodePacket.wrap(new String(bytes, StandardCharsets.UTF_8), bytes.length);
        }
      };

  public static void main(String[] args) {
    TcpClient<String> client = new TcpClient<>("localhost", 1024);
    client.setSync(true);
    client.setPacketCoder(packetCoder);
    client.setTimeout(30000);
    client.start();
    for (int i = 0; i < 100; i++) {
      String resp = client.send("hello");
      System.out.println(resp);
      ThreadUtil.sleep(1000);
    }
  }
}
