package me.hekr.iotos.softgateway.common.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/** 用于格式转换以及hash加密算法 */
public class ParseUtil {
  /** hash加密方法 */
  public static final String HASH_METHOD = "HmacSHA1";
  /** sign值计算 */
  public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
    byte[] data = encryptKey.getBytes();
    // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
    SecretKey secretKey = new SecretKeySpec(data, HASH_METHOD);
    // 生成一个指定 Mac 算法 的 Mac 对象
    Mac mac = Mac.getInstance(HASH_METHOD);
    // 用给定密钥初始化 Mac 对象
    mac.init(secretKey);

    byte[] text = encryptText.getBytes();
    // 完成 Mac 操作
    return mac.doFinal(text);
  }

  /** 数据格式转为string */
  public static String parseByte2HexStr(byte[] buf) {
    if (null == buf) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  /** byte转为十进制int */
  public static int byte2int(byte bytes) {
    // 将byte转换为8位二进制字符串 依赖 commons-lang-x.x.jar包
    String binaryString =
        String.format("%8s", Integer.toBinaryString(bytes & 0xFF)).replace(' ', '0');
    // 将二进制字符串转换为十进制整数值
    return Integer.parseInt(binaryString, 2);
  }
}
