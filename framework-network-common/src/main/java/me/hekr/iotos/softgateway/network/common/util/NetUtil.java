package me.hekr.iotos.softgateway.network.common.util;

import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/** @author iotos */
@Slf4j
public class NetUtil {
  public static void close(EventLoopGroup group) {
    if (group != null) {
      try {
        group.shutdownGracefully();
      } catch (Exception e) {
        log.warn(e.getMessage(), e);
      }
    }
  }
}
