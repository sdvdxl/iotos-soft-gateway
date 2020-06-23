package com.example.iotcloudgateway.tcp;

import com.example.iotcloudgateway.codec.DataCodec;
import com.example.iotcloudgateway.codec.LinePacketCodec;
import com.example.iotcloudgateway.codec.RawDataCodec;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.constant.SubKlinkAction;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

@Slf4j
public class TcpServerMsgHandler implements ServerAioHandler {
  private LinePacketCodec packetCodec = new LinePacketCodec();
  private DataCodec dataCodec = new RawDataCodec();

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

  /** 处理部分 */
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

    if (channelContext.userid == null) {
      Tio.bindUser(channelContext, klinkDev.getPk() + "@" + klinkDev.getDevId());
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    switch (klinkDev.getAction()) {
      case SubKlinkAction.ADD_TOPO:
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_LOGIN:
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId());
        MqttServer.devLogin(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_LOGOUT:
        MqttServer.devLogout(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.GET_TOPO:
        MqttServer.devTopo();
        break;
      case SubKlinkAction.DEL_TOPO:
        MqttServer.delDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case SubKlinkAction.DEV_SEND:
        MqttServer.devSend(JsonUtil.toJson(klinkDev));
        break;
      case SubKlinkAction.HEARTBEAT:
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + klinkDev.getAction());
    }
    Tio.send(channelContext, packet);
  }
}
