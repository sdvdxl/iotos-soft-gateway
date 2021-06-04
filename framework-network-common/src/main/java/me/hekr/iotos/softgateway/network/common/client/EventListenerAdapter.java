package me.hekr.iotos.softgateway.network.common.client;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.common.ConnectionContext;

/**
 * 如果不想实现所有事件监听，可以继承这个类
 *
 * @author iotos
 */
@Slf4j
public class EventListenerAdapter<T> implements EventListener<T> {

  @Override
  public void onConnect(ConnectionContext<T> ctx) {
    log.info("onConnect, remote: {}", ctx.getAddress());
  }

  @Override
  public void onDisconnect(ConnectionContext<T> ctx, CloseReason reason) {
    log.info("onDisconnect, remote: {}, reason: {}", ctx.getAddress(), reason);
  }

  @Override
  public void onHeartbeatTimeout(ConnectionContext<T> ctx, LocalDateTime lastOccurTime, int count) {}

  @Override
  public void exceptionCaught(ConnectionContext<T> ctx, Throwable t) {
    log.error("exceptionCaught, remote: " + ctx.getAddress() + ", error:" + t.getMessage(), t);
  }
}
