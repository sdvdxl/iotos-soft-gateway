package me.hekr.iotos.softgateway.example.tcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/** @author iotos */
@Slf4j
@Service
public class TcpClientListener implements ClientAioListener {

  @Override
  public void onAfterConnected(ChannelContext channelContext, boolean b, boolean b1)
      throws Exception {}

  @Override
  public void onAfterDecoded(ChannelContext channelContext, Packet packet, int i)
      throws Exception {}

  @Override
  public void onAfterReceivedBytes(ChannelContext channelContext, int i) throws Exception {}

  @Override
  public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b)
      throws Exception {}

  @Override
  public void onAfterHandled(ChannelContext channelContext, Packet packet, long l)
      throws Exception {}

  @Override
  public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String s, boolean b)
      throws Exception {}
}
