package me.hekr.iotos.softgateway.sample;

import cn.hutool.core.thread.ThreadUtil;
import me.hekr.iotos.softgateway.network.udp.UdpClient;

/**
 * udp 客户端
 *
 * @author iotos
 */
public class UdpClientSample {

  public static void main(String[] args) {
    UdpClient<String> client = new UdpClient<>("localhost", UdpServerSample.BIND_PORT, 0);
    client.setMessageListener(
        ctx -> {
          System.out.println("收到来自 " + ctx.getAddress() + " 的消息：" + ctx.getMessage());
        });
    client.setPacketCoder(UdpServerSample.PACKET_CODER);
    client.setEnableNetLog(false);
    client.start();
    while (true) {
      client.send("hello");
      ThreadUtil.sleep(1000);
    }
  }
}
