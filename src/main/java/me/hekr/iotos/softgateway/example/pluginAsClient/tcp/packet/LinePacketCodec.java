package me.hekr.iotos.softgateway.example.pluginAsClient.tcp.packet;

import cn.hutool.core.util.ArrayUtil;
import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.utils.ParseUtil;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

/** 此类表示拆包方式为以换行符"\n"作为分割标识进行拆包 */
@Slf4j
@Component
public class LinePacketCodec implements PacketCodec {
  public static final byte HEAD = (byte) 0xff;

  public static final int baseLength = 4;

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
    if (readableLength < baseLength) {
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

    // 具体内容开始的index
    int contentStartReader;
    // 具体内容结束的index
    int contentEndReader;

    int i = 0;
    for (; i < readableLength; i++) {
      byte b = buffer.get(position + i);
      if (b == HEAD) {
        buffer.get();
        break;
      }
    }
    // 没有起始符，返回null，等待下次解码
    if (i + baseLength > readableLength) {
      return null;
    }

    byte cmdByte = buffer.get();

    byte lengthByte = buffer.get();

    int length = ParseUtil.byte2int(lengthByte);

    // 若不够数据长度，返回null，等待下次解码
    if (i + length + baseLength > readableLength) {
      return null;
    }

    byte[] body = new byte[length + baseLength - 1];
    body[0] = HEAD;
    body[1] = cmdByte;
    body[2] = lengthByte;
    buffer.get(body, 3, length);

    byte flagByte = buffer.get();
    byte flag = bytesXOR(body);
    if (flagByte == flag) {
      TcpPacket imPacket = new TcpPacket();
      imPacket.setBody(body);
      return imPacket;
    }
    return new TcpPacket();
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
    ByteBuffer buffer = ByteBuffer.allocate(bodyLen + 2);
    // 设置字节序
    buffer.order(tioConfig.getByteOrder());

    // 写入消息体
    if (body != null) {
      buffer.put(HEAD);
      buffer.put(body);
      byte[] bytes = new byte[1];
      bytes[0] = HEAD;
      byte[] all = ArrayUtil.addAll(bytes, body);
      buffer.put(bytesXOR(all));
    }

    return buffer;
  }

  private byte bytesXOR(byte[] bytes) {
    byte res = bytes[0];
    for (int i = 1; i < bytes.length; i++) {
      res = (byte) (res ^ bytes[i]);
    }
    return res;
  }
}
