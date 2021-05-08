package me.hekr.iotos.softgateway.network.udp;

import cn.hutool.core.thread.ThreadUtil;
import java.nio.charset.Charset;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
import me.hekr.iotos.softgateway.network.tcp.TcpClient;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TcpClientTest {
  PacketCoder<String> packetCoder =
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

  @Test
  public void testSendAsync() {
    TcpClient<String> client = new TcpClient<>("localhost", 1024);

    client.setMessageListener(
        ctx -> System.out.println("收到来自 " + ctx.getAddress() + " 的消息：" + ctx.getMessage()));
    client.setPacketCoder(packetCoder);
    client.start();
    for (int i = 0; i < 100; i++) {
      client.send("hello" + i);
      ThreadUtil.sleep(1000);
    }
  }

  @Test
  public void testSendSync() {
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
    //    Assert.notNull(resp);
  }
}
