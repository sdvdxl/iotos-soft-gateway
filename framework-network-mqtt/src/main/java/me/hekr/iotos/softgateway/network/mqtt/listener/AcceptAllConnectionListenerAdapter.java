package me.hekr.iotos.softgateway.network.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.mqtt.ConnectionContext;

/** @author iotos */
@Slf4j
public class AcceptAllConnectionListenerAdapter<T> extends AbstractListenerAdapter<T> {

  @Override
  public boolean auth(ConnectionContext<T> context) {
    log.info("接受连接，clientId：{}，address:{}", context.getClientId(), context.getAddress());
    return true;
  }
}
