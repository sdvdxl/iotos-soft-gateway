package me.hekr.iotos.softgateway.udp.client;

/** @author iotos */
public interface UdpCoder<T> {

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
   * @param bytes 输入字节
   * @return 解码后的对象
   */
  Object decode(byte[] bytes);
}
