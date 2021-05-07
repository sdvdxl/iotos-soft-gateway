package me.hekr.iotos.softgateway.network.common;

/** @author iotos */
public interface PacketCoder<T> {

  /**
   * 编码
   *
   * @param t 输出数据
   * @return 将 t 转换为字节数组，如果为 null 或者为空则不会发送
   */
  byte[] encode(T t);

  /**
   * 解码数据
   *
   * <p>提示：之所以用bytes 入参，是为了减少对 netty 的依赖，使开发者直接使用熟悉的字节操作。
   *
   * <p>读取之后，如果解码成功，需要设置读取的字节数，用来设置内置缓冲区。
   *
   * <p>如果没有解码成功，可以返回 null 或者 DecodePacket.NULL
   *
   * @param bytes 输入字节
   * @return 解码后的对象,和读取的字节长度
   */
  DecodePacket decode(byte[] bytes);
}
