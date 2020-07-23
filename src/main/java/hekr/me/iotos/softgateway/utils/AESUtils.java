package hekr.me.iotos.softgateway.utils;

import com.alibaba.fastjson.JSON;
import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.tio.utils.jfinal.P;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** AES加密类 */
public class AESUtils {

  /** 密钥算法 */
  private static final String KEY_ALGORITHM = "AES";

  private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

  private static String KEY = ProxyConfig.AES_KEY;

  /**
   * 初始化密钥
   *
   * @return byte[] 密钥
   * @throws Exception
   */
  public static byte[] initSecretKey() {
    // 返回生成指定算法的秘密密钥的 KeyGenerator 对象
    KeyGenerator kg = null;
    try {
      kg = KeyGenerator.getInstance(KEY_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return new byte[0];
    }
    // 初始化此密钥生成器，使其具有确定的密钥大小
    // AES 要求密钥长度为 128
    kg.init(128);
    // 生成一个密钥
    SecretKey secretKey = kg.generateKey();
    return secretKey.getEncoded();
  }

  /**
   * 转换密钥
   *
   * @param key 二进制密钥
   * @return 密钥
   */
  public static Key toKey(byte[] key) {
    // 生成密钥
    return new SecretKeySpec(key, KEY_ALGORITHM);
  }

  /**
   * 加密
   *
   * @param data 待加密数据
   * @param key 密钥
   * @return byte[] 加密数据
   * @throws Exception
   */
  public static byte[] encrypt(byte[] data, Key key) throws Exception {
    return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
  }

  /**
   * 加密
   *
   * @param data 待加密数据
   * @param key 二进制密钥
   * @return byte[] 加密数据
   * @throws Exception
   */
  public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
    return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
  }

  /**
   * 加密
   *
   * @param data 待加密数据
   * @param key 二进制密钥
   * @param cipherAlgorithm 加密算法/工作模式/填充方式
   * @return byte[] 加密数据
   * @throws Exception
   */
  public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
    // 还原密钥
    Key k = toKey(key);
    return encrypt(data, k, cipherAlgorithm);
  }

  /**
   * 加密
   *
   * @param data 待加密数据
   * @param key 密钥
   * @param cipherAlgorithm 加密算法/工作模式/填充方式
   * @return byte[] 加密数据
   * @throws Exception
   */
  public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
    // 实例化
    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
    // 使用密钥初始化，设置为加密模式
    cipher.init(Cipher.ENCRYPT_MODE, key);
    // 执行操作
    return cipher.doFinal(data);
  }

  /**
   * 解密
   *
   * @param data 待解密数据
   * @param key 二进制密钥
   * @return byte[] 解密数据
   * @throws Exception
   */
  public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
    return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
  }

  /**
   * 解密
   *
   * @param data 待解密数据
   * @param key 密钥
   * @return byte[] 解密数据
   * @throws Exception
   */
  public static byte[] decrypt(byte[] data, Key key) throws Exception {
    return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
  }

  /**
   * 解密
   *
   * @param data 待解密数据
   * @param key 二进制密钥
   * @param cipherAlgorithm 加密算法/工作模式/填充方式
   * @return byte[] 解密数据
   * @throws Exception
   */
  public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
    // 还原密钥
    Key k = toKey(key);
    return decrypt(data, k, cipherAlgorithm);
  }

  /**
   * 解密
   *
   * @param data 待解密数据
   * @param key 密钥
   * @param cipherAlgorithm 加密算法/工作模式/填充方式
   * @return byte[] 解密数据
   * @throws Exception
   */
  public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
    // 实例化
    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
    // 使用密钥初始化，设置为解密模式
    cipher.init(Cipher.DECRYPT_MODE, key);
    // 执行操作
    return cipher.doFinal(data);
  }

  public static String showByteArray(byte[] data) {
    if (null == data) {
      return null;
    }
    StringBuilder sb = new StringBuilder("{");
    for (byte b : data) {
      sb.append(b).append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append("}");
    return sb.toString();
  }

  /**
   * 将16进制转换为二进制
   *
   * @param hexStr
   * @return
   */
  public static byte[] parseHexStr2Byte(String hexStr) {

    if (hexStr.length() < 1) return null;
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }

  /**
   * 将二进制转换成16进制
   *
   * @param buf
   * @return
   */
  public static String parseByte2HexStr(byte buf[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }

  /**
   * @param str
   * @param key
   * @return
   * @throws Exception
   */
  public static String aesEncrypt(String str, String key) throws Exception {
    if (str == null || key == null) return null;
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
    byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
    return new BASE64Encoder().encode(bytes);
  }

  public static String aesDecrypt(String str, String key) throws Exception {
    if (str == null || key == null) return null;
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
    byte[] bytes = new BASE64Decoder().decodeBuffer(str);
    bytes = cipher.doFinal(bytes);
    return new String(bytes, "utf-8");
  }

  public static Key getKey() {
    return toKey(Base64.decodeBase64(KEY));
  }

  @SneakyThrows
  public static String encodeBody(String body) {
    byte[] encrypt = encrypt(body.getBytes("utf-8"), getKey());
    return parseByte2HexStr(encrypt);
  }

  @SneakyThrows
  public static byte[] decodeRequestData(String data) {
    return decrypt(parseHexStr2Byte(data), getKey());
  }

  public static void main(String[] args) throws Exception {
    byte[] key = initSecretKey();
    System.out.println("key：" + Base64.encodeBase64String(key));
    System.out.println("key：" + showByteArray(key));

    // 指定key
    String kekkk = "cmVmb3JtZXJyZWZvcm1lcg==";
    System.out.println("kekkk:" + showByteArray(Base64.decodeBase64(kekkk)));
    Key k = toKey(Base64.decodeBase64(kekkk));

    String data =
        "{\"requestName\":\"BeforeIn\",\"requestValue\":{\"carCode\":\"浙AD0V07\",\"inTime\":\"2016-09-29 10:06:03\",\"inChannelId\":\"4\",\"GUID\":\"1403970b-4eb2-46bc-8f2b-eeec91ddcd5f\",\"inOrOut\":\"0\"},\"Type\":\"0\"}";
    System.out.println("加密前数据: string:" + data);
    System.out.println("加密前数据: byte[]:" + showByteArray(data.getBytes("utf-8")));
    System.out.println();

    byte[] encryptData = encrypt(data.getBytes("utf-8"), k);
    String encryptStr = parseByte2HexStr(encryptData);

    System.out.println("加密后数据: byte[]:" + showByteArray(encryptData));
    System.out.println("加密后数据: Byte2HexStr:" + encryptStr);
    System.out.println();

    String s =
        "C0DA15A4AB999B3E527F09D62ABBB2B2C8BA060A00A973B04AD5799E6AA191B0BE59C5F8407526228C2BB73BCD65173D3AF396C10845BE1F52EC3C1E50B2962F66F3942D77561BC3325CDED1D8BC4B7AC56AE9F1F376B7736570AEC364203FE8C0886E15D2D026CBC6CD218126DE858EE17A8D5D6415C71BC2DA7FF073C5DC590500F2FFEF5D248AA57EED97438644411B2E2F846CA0B23AF29827D2BD8B152F67DAA9CD391E45C0C92F08C15D5584AB7621F0DBE30A3071A2A5841AA831054858E1E639AB95D9AA6F3F395C68176CEF237DBAC164F717A90CF3228C64C220DB";

    byte[] decryptData = decrypt(parseHexStr2Byte(s), k);
    System.out.println("解密后数据: byte[]:" + showByteArray(decryptData));
    System.out.println("解密后数据: string:" + new String(decryptData, "utf-8"));
    EntranceReq entranceReq = JsonUtil
        .fromJson(new String(decryptData, "utf-8"), EntranceReq.class);
    System.out.println(entranceReq);
  }
}
