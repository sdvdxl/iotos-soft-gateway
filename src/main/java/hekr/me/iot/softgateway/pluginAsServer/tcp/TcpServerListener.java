package hekr.me.iot.softgateway.pluginAsServer.tcp;

import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;

/** tcp server的监听模块， */
@Slf4j
public class TcpServerListener implements ServerAioListener {

  @Override
  public boolean onHeartbeatTimeout(
      ChannelContext channelContext, Long interval, int heartbeatTimeoutCount) {
    log.debug(
        "{}:{} heartbeat timeout :{}",
        channelContext.getClientNode().getIp(),
        channelContext.getClientNode().getPort(),
        channelContext.userid);
    return false;
  }

  @Override
  public void onAfterConnected(
      ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {}

  @Override
  public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize)
      throws Exception {}

  @Override
  public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes)
      throws Exception {}

  @Override
  public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess)
      throws Exception {}

  @Override
  public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost)
      throws Exception {}

  @Override
  public void onBeforeClose(
      ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove)
      throws Exception {
    log.debug(
        "{}:{} 断开连接",
        channelContext.getClientNode().getIp(),
        channelContext.getClientNode().getPort());
    // 断连时对已完成登录校验的设备进行登出操作
    if (channelContext.userid != null) {}
  }
}
