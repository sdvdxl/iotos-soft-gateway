package me.hekr.iotos.softgateway.sample;

import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;
import me.hekr.iotos.softgateway.network.mqtt.MqttServer;
import me.hekr.iotos.softgateway.network.mqtt.listener.AcceptAllListenerAdapter;

/** @author iotos */
@Slf4j
public class MqttServerSample {
  public static void main(String[] args) {
    try {
      MqttServer<String> mqttServer = new MqttServer<>();
      mqttServer.setPacketCoder(PacketCoder.STRING_CODER);
      mqttServer.setListener(new AcceptAllListenerAdapter<>());
      mqttServer.start();
      new CountDownLatch(1).await();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
