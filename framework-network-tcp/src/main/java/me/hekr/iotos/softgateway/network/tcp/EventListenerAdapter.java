package me.hekr.iotos.softgateway.network.tcp;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;

/**
 * 如果不想实现所有事件监听，可以继承这个类
 *
 * @author iotos
 */
@Slf4j
public class EventListenerAdapter<T> implements EventListener<T> {

  @Override
  public void onConnect(TcpServerPacketContext<T> ctx) {
    log.info("onConnect, remote: {}", ctx.getAddress());
  }

  @Override
  public void onDisconnect(TcpServerPacketContext<T> ctx, CloseReason reason) {
    log.info("onDisconnect, remote: {}, reason: {}", ctx.getAddress(), reason);
  }

  @Override
  public void onHeartbeatTimeout(
      TcpServerPacketContext<T> ctx, LocalDateTime lastOccurTime, int count) {
    log.warn(
        "onHeartbeatTimeout close connection, remote: {}, lastOccurTime: {}, count: {}",
        ctx.getAddress(),
        lastOccurTime,
        count);
    ctx.setCloseReason(CloseReason.HEARTBEAT_TIMEOUT);
    ctx.close();
  }

  @Override
  public void exceptionCaught(TcpServerPacketContext<T> ctx, Throwable t) {
    log.error("exceptionCaught, remote: " + ctx.getAddress() + ", error:" + t.getMessage(), t);
  }
}
