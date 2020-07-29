package hekr.me.iotos.softgateway.utils;

import cn.hutool.core.convert.Convert;

/**
 * @author jiatao
 * @date 2020/7/29
 */
public class SignUtil {
  public static String GetSignature(String timeStamp, String nonce, String tokenKey, String data) {
    // 拼接签名数据
    String signStr = timeStamp + nonce + tokenKey + data;
    byte[] bytes = signStr.getBytes();
    return DataSummary(bytes);
  }

  private static String DataSummary(byte[] sourceBytes) {
    // 简单求和
    byte resultByte = 0x00;
    for (byte sourceByte : sourceBytes) {
      resultByte += sourceByte;
    }
    return Integer.toHexString(resultByte);
  }
}
