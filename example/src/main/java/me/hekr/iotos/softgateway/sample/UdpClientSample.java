package me.hekr.iotos.softgateway.sample;

import cn.hutool.core.thread.ThreadUtil;
import me.hekr.iotos.softgateway.network.udp.UdpClient;

/**
 * udp 客户端
 *
 * @author iotos
 */
public class UdpClientSample {
  private static final boolean sync = false;

  public static void main(String[] args) {
    UdpClient<String> client = new UdpClient<>("localhost", UdpServerSample.BIND_PORT, 1021);
    client.setMessageListener(
        ctx -> System.out.println("收到来自 " + ctx.getAddress() + " 的消息：" + ctx.getMessage()));
    client.setPacketCoder(UdpServerSample.PACKET_CODER);
    client.setEnableNetLog(false);
    client.setSync(sync);
    // udp 不起作用
    client.setAutoReconnect(true);
    // 同步模式设置超时时间
    if (sync) {
      client.setTimeout(3000);
    }
    client.start();
    while (true) {
      String resp = client.send("hello");
      if (sync) {
        System.out.println("收到回复: " + resp);
      }
      ThreadUtil.sleep(1000);
    }
  }
}
