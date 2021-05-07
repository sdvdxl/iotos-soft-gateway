package me.hekr.iotos.softgateway.network.udp.client;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class UdpClientTest {
  UdpCoder<String> udpCoder =
      new UdpCoder<String>() {
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
    UdpClient<String> client = new UdpClient<>();
    client.setHost("localhost");
    client.setPort(1024);
    client.setBindPort(1021);

    client.setMessageListener(
        new UdpMessageListener<String>() {
          @Override
          public void onMessage(SocketAddress addr, String msg) {
            System.out.println("收到来自 " + addr + " 的消息：" + msg);
          }
        });
    client.setUdCoder(udpCoder);
    client.init();
    client.send("hello");
    ThreadUtil.sleep(10000000);
  }

  @Test
  public void testSendSync() {
    UdpClient<String> client = new UdpClient<>();
    client.setHost("localhost");
    client.setPort(1024);
    client.setSync(true);
    client.setUdCoder(udpCoder);
    client.setBindPort(1021);
    client.setTimeout(3000);
    client.init();
    String resp = client.send("hello");
    System.out.println(resp);
    Assert.notNull(resp);
  }
}
