package me.hekr.iotos.softgateway.network.udp;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import java.nio.charset.Charset;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class UdpClientTest {
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
    UdpClient<String> client = new UdpClient<>("localhost", 1024, 1021);
    client.setMessageListener(
        ctx -> System.out.println("收到来自 " + ctx.getAddress() + " 的消息：" + ctx.getMessage()));
    client.setPacketCoder(packetCoder);
    client.start();
    client.send("hello");
    ThreadUtil.sleep(10000000);
  }

  @Test
  public void testSendSync() {
    UdpClient<String> client = new UdpClient<>("localhost", 1024, 1021);
    client.setSync(true);
    client.setPacketCoder(packetCoder);
    client.setTimeout(30000);
    client.start();
    String resp = client.send("hello");
    System.out.println(resp);
    Assert.notNull(resp);
  }
}
