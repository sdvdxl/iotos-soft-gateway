package hekr.me.iot.softgateway.pluginAsClient.tcp;

import hekr.me.iot.softgateway.common.klink.DevSend;
import hekr.me.iot.softgateway.pluginAsServer.tcp.codec.DataCodec;
import hekr.me.iot.softgateway.pluginAsServer.tcp.codec.LinePacketCodec;
import hekr.me.iot.softgateway.pluginAsServer.tcp.codec.RawDataCodec;
import hekr.me.iot.softgateway.northProxy.MqttServer;
import hekr.me.iot.softgateway.pluginAsServer.tcp.TcpPacket;

import java.nio.ByteBuffer;

import lombok.extern.slf4j.Slf4j;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

@Slf4j
public class TcpClientHandler implements ClientAioHandler {
  private LinePacketCodec packetCodec = new LinePacketCodec();
  private DataCodec dataCodec = new RawDataCodec();

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

    DevSend klinkDev = dataCodec.decode(tcpPacket, channelContext);

    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      return;
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    MqttServer.sendKlink(klinkDev);
  }
}
