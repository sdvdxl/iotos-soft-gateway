package com.example.iotcloudgateway.server.tcp;

import com.example.iotcloudgateway.codec.DataCodec;
import com.example.iotcloudgateway.codec.LinePacketCodec;
import com.example.iotcloudgateway.codec.RawDataCodec;
import com.example.iotcloudgateway.klink.DevSend;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * tcp 服务端信息处理部分，在此处调用拆包、组包的方法，以及编解码部分。
 * 具体拆包和解码的方法用户可以自行实现com.example.iotcloudgateway.codec中的DataCodec和PacketCodec接口进行二次开发。
 */
@Slf4j
public class TcpServerMsgHandler implements ServerAioHandler {
  /**
   * 本示例代码分别列举了一种拆包方式和一种业务数据处理方式
   *
   * <p>此处为以换行符'\n'作为拆包标志的方式进行拆包
   */
  private LinePacketCodec packetCodec = new LinePacketCodec();

  /** 此处以透传方式处理业务数据，详情可参看使用手册和源码 */
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

    // 此处调用业务数据处理的方法对透传数据进行处理，处理后的结果为klink格式
    DevSend klinkDev = dataCodec.decode(tcpPacket, channelContext);

    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      return;
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    switch (klinkDev.getAction()) {
      case SubKlinkAction.ADD_TOPO:
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getDevSecret());
        break;
      case SubKlinkAction.DEV_LOGIN:
        MqttServer.addDev(klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getDevSecret());
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
  }
}
