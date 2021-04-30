package me.hekr.iotos.softgateway.pluginAsClient.tcp;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.codec.DataCodec;
import me.hekr.iotos.softgateway.common.klink.KlinkDev;
import me.hekr.iotos.softgateway.common.klink.KlinkService;
import me.hekr.iotos.softgateway.pluginAsClient.tcp.packet.LinePacketCodec;
import me.hekr.iotos.softgateway.pluginAsClient.tcp.packet.TcpPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

@Slf4j
@Service
public class TcpClientHandler implements ClientAioHandler {
  @Autowired private LinePacketCodec packetCodec;
  @Autowired private DataCodec dataCodec;
  @Autowired private KlinkService klinkService;

  /** 此处由于发送心跳包，可在此处自定义心跳包格式 */
  @Override
  public Packet heartbeatPacket(ChannelContext channelContext) {
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(TcpPacket.HEARTBEAT);
    return tcpPacket;
  }

  /** 拆包部分 */
  @Override
  public Packet decode(
      ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
      throws AioDecodeException {
    return packetCodec.decode(buffer, limit, position, readableLength, channelContext);
  }

  /** 组包部分 */
  @Override
  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    return packetCodec.encode(packet, tioConfig, channelContext);
  }

  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    TcpPacket tcpPacket = (TcpPacket) packet;
    if (((TcpPacket) packet).getBody() == null) {
      // 解码时因特殊原因丢弃此帧数据，故此处不做处理
      return;
    }

    KlinkDev klinkDev = dataCodec.decode(tcpPacket);

    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      return;
    }

    klinkService.devSend(klinkDev);
    // 对转码后的数据按照klink的action进行不同业务的操作
    //    ProxyService.sendKlink(klinkDev);
  }
}
