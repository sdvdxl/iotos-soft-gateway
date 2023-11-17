package me.hekr.iotos.softgateway.network.common.util;

import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>NetUtil class.</p>
 *
 * @version $Id: $Id
 */
@Slf4j
public class NetUtil {
  /**
   * <p>close.</p>
   *
   * @param group a {@link io.netty.channel.EventLoopGroup} object.
   */
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
