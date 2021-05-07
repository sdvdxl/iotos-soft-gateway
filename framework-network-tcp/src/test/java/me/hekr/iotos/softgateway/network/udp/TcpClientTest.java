package me.hekr.iotos.softgateway.network.udp;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import me.hekr.iotos.softgateway.network.common.MessageListener;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
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
        public Object decode(byte[] bytes) {
          return new String(bytes, Charset.forName("GBK"));
        }
      };

  @Test
  public void testSendAsync() {
    TcpClient<String> client = new TcpClient<>("localhost", 1024);

    client.setMessageListener(
        new MessageListener<String>() {
          @Override
          public void onMessage(SocketAddress addr, String msg) {
            System.out.println("收到来自 " + addr + " 的消息：" + msg);
          }
        });
    client.setPacketCoder(packetCoder);
    client.start();
    client.send("hello");
    ThreadUtil.sleep(10000000);
  }

  @Test
  public void testSendSync() {
    TcpClient<String> client = new TcpClient<>("localhost", 1024);
    client.setSync(true);
    client.setPacketCoder(packetCoder);
    client.setTimeout(3000);
    client.start();
    String resp = client.send("hello");
    System.out.println(resp);
    Assert.notNull(resp);
  }
}
