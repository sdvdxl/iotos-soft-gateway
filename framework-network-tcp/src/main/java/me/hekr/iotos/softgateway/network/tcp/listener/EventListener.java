package me.hekr.iotos.softgateway.network.tcp.listener;

import java.time.LocalDateTime;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.tcp.TcpServerPacketContext;

/**
 * 新的客户端连接上来监听
 *
 * @author iotos
 */
public interface EventListener<T> {

  /**
   * 客户端与服务端建立连接
   *
   * @param ctx
   */
  void onConnect(TcpServerPacketContext<T> ctx);

  /**
   * 客户端与服务端断开连接
   *
   * @param ctx
   * @param reason 关闭连接原因
   * @param t 异常关闭时候的异常
   */
  void onDisconnect(TcpServerPacketContext<T> ctx, CloseReason reason);

  /**
   * * 客户端不活跃（超过了设定的心跳超时时间）
   *
   * @param ctx
   * @param lastOccurTime 上次发生时间，如果是第一次，则为 null
   * @param count 连续发生次数，如果有数据进来就会被重置为0开始
   */
  void onHeartbeatTimeout(TcpServerPacketContext<T> ctx, LocalDateTime lastOccurTime, int count);

  /**
   * 异常处理
   *
   * @param ctx
   * @param t
   */
  void exceptionCaught(TcpServerPacketContext<T> ctx, Throwable t);
}