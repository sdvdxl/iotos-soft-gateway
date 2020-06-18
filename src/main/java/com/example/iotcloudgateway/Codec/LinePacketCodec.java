package com.example.iotcloudgateway.Codec;

import com.example.iotcloudgateway.dto.TcpPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * 此类表示拆包方式为以换行符"\n"作为分割标识进行拆包
 *
 */
@Slf4j
@Component
public class LinePacketCodec implements PacketCodec {
  public static final byte SPLIT = '\n';

  /**
   * 消息解码
   *
   * @param buffer
   * @param limit
   * @param position
   * @param readableLength
   * @param channelContext
   * @return
   * @throws AioDecodeException
   */
  @Override
  public TcpPacket decode(
          ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
      throws AioDecodeException {
    // 消息不够组包直接返回，等待后续的包
    if (readableLength <= 0) {
      return null;
    }

    // 防止socket字节流攻击
    if (readableLength > TcpPacket.LEN_MAX) {
      byte[] clear = new byte[readableLength];
      buffer.get(clear);
      log.warn(
          "packet too large, from {}:{}, device info:{}, length:{}, data:{}",
          channelContext.getClientNode().getIp(),
          channelContext.getClientNode().getPort(),
          channelContext.userid,
          readableLength,
          clear);
      // 丢弃该帧
      return new TcpPacket();
    }

    int i = 0;
    for (; i < readableLength; i++) {
      byte b = buffer.get(position + i);
      if (b == SPLIT) {
        break;
      }
    }
    // 没有结束符，返回null，等待下次解码
    if (i == readableLength) {
      return null;
    }
    byte[] dst = new byte[i + 1];
    buffer.get(dst);
    TcpPacket imPacket = new TcpPacket();
    imPacket.setBody(dst);
    return imPacket;
  }

  /**
   * 消息编码
   *
   * @param packet
   * @param tioConfig
   * @param channelContext
   * @return
   */
  @Override
  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    TcpPacket tcpPacket = (TcpPacket) packet;
    byte[] body = tcpPacket.getBody();
    int bodyLen = 0;
    if (body == null) {
      log.warn(
          "send void message from {}:{}, device info:{}",
          channelContext.getClientNode().getIp(),
          channelContext.getClientNode().getPort(),
          channelContext.userid);
    }
    if (body != null) {
      bodyLen = body.length;
    }
    // 创建一个新的bytebuffer
    ByteBuffer buffer = ByteBuffer.allocate(bodyLen);
    // 设置字节序
    buffer.order(tioConfig.getByteOrder());

    // 写入消息体
    if (body != null) {
      buffer.put(body);
    }

    return buffer;
  }
}
