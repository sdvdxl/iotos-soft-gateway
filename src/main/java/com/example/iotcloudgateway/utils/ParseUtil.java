package com.example.iotcloudgateway.utils;

import com.example.iotcloudgateway.constant.Constants;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ParseUtil {
  /** sign值计算 */
  public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
    byte[] data = encryptKey.getBytes();
    // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
    SecretKey secretKey = new SecretKeySpec(data, Constants.HASH_METHOD);
    // 生成一个指定 Mac 算法 的 Mac 对象
    Mac mac = Mac.getInstance(Constants.HASH_METHOD);
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
}
