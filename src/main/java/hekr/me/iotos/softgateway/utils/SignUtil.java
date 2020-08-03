package hekr.me.iotos.softgateway.utils;

/**
 * @author jiatao
 * @date 2020/7/29
 */
public class SignUtil {
  public static String getSignature(String timeStamp, String nonce, String tokenKey, String data) {
    String s = data.replaceAll("=", "").replaceAll("&", "");
    // 拼接签名数据
    String signStr = timeStamp + nonce + tokenKey + s;
    byte[] bytes = signStr.getBytes();
    return dataSummary(bytes);
  }

  private static String dataSummary(byte[] sourceBytes) {
    // 简单求和
    byte resultByte = 0x00;
    for (byte sourceByte : sourceBytes) {
      resultByte += sourceByte;
    }
    return ParseUtil.parseByte2HexStr(new byte[] {resultByte});
  }
}
