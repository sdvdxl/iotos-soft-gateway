package me.hekr.iotos.softgateway.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.TimeZone;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;

/**
 * json工具类，用于完成对象与json之间的转换
 *
 * <p>时间格式化默认为 GMT+8
 *
 * @author du
 * @version $Id: $Id
 */
public class JsonUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
    objectMapper.findAndRegisterModules();
    objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
  }

  /**
   * <p>parse.</p>
   *
   * @param str a {@link java.lang.String} object
   * @return a {@link com.fasterxml.jackson.databind.JsonNode} object
   */
  @SneakyThrows
  public static JsonNode parse(String str) {
    return objectMapper.readValue(str, JsonNode.class);
  }

  /**
   * <p>toJson.</p>
   *
   * @param obj a {@link java.lang.Object} object
   * @return a {@link java.lang.String} object
   */
  @SneakyThrows
  public static String toJson(Object obj) {
    return objectMapper.writeValueAsString(obj);
  }

  /**
   * <p>toBytes.</p>
   *
   * @param obj a {@link java.lang.Object} object
   * @return an array of {@link byte} objects
   */
  @SneakyThrows
  public static byte[] toBytes(Object obj) {
    return objectMapper.writeValueAsBytes(obj);
  }

  /**
   * <p>toBase64.</p>
   *
   * @param obj a {@link java.lang.Object} object
   * @return a {@link java.lang.String} object
   */
  @SneakyThrows
  public static String toBase64(Object obj) {
    return Base64.encodeBase64String(objectMapper.writeValueAsBytes(obj));
  }

  /**
   * <p>fromJson.</p>
   *
   * @param payload a {@link java.lang.String} object
   * @param tClass a {@link java.lang.Class} object
   * @param <T> a T class
   * @return a T object
   */
  @SneakyThrows
  public static <T> T fromJson(String payload, Class<T> tClass) {
    return objectMapper.readValue(payload, tClass);
  }

  /**
   * <p>fromJson.</p>
   *
   * @param payload a {@link java.lang.String} object
   * @param tTypeReference a {@link com.fasterxml.jackson.core.type.TypeReference} object
   * @param <T> a T class
   * @return a T object
   */
  @SneakyThrows
  public static <T> T fromJson(String payload, TypeReference<T> tTypeReference) {
    return objectMapper.readValue(payload, tTypeReference);
  }

  /**
   * <p>fromBase64.</p>
   *
   * @param payload a {@link java.lang.String} object
   * @param tClass a {@link java.lang.Class} object
   * @param <T> a T class
   * @return a T object
   */
  @SneakyThrows
  public static <T> T fromBase64(String payload, Class<T> tClass) {
    return objectMapper.readValue(Base64.decodeBase64(payload), tClass);
  }

  /**
   * <p>fromBytes.</p>
   *
   * @param payload an array of {@link byte} objects
   * @param tClass a {@link java.lang.Class} object
   * @param <T> a T class
   * @return a T object
   */
  @SneakyThrows
  public static <T> T fromBytes(byte[] payload, Class<T> tClass) {
    return objectMapper.readValue(payload, tClass);
  }

  /**
   * <p>fromBytes.</p>
   *
   * @param payload an array of {@link byte} objects
   * @param tTypeReference a {@link com.fasterxml.jackson.core.type.TypeReference} object
   * @param <T> a T class
   * @return a T object
   */
  @SneakyThrows
  public static <T> T fromBytes(byte[] payload, TypeReference<T> tTypeReference) {
    return objectMapper.readValue(payload, tTypeReference);
  }

  /**
   * <p>fromBase64.</p>
   *
   * @param payload a {@link java.lang.String} object
   * @param tTypeReference a {@link com.fasterxml.jackson.core.type.TypeReference} object
   * @param <T> a T class
   * @return a T object
   */
  @SneakyThrows
  public static <T> T fromBase64(String payload, TypeReference<T> tTypeReference) {
    return objectMapper.readValue(Base64.decodeBase64(payload), tTypeReference);
  }

  /**
   * <p>convert.</p>
   *
   * @param obj a {@link java.lang.Object} object
   * @param clazz a {@link java.lang.Class} object
   * @param <T> a T class
   * @return a T object
   */
  public static <T> T convert(Object obj, Class<T> clazz) {
    return objectMapper.convertValue(obj, clazz);
  }

  /**
   * <p>convert.</p>
   *
   * @param obj a {@link java.lang.Object} object
   * @param tTypeReference a {@link com.fasterxml.jackson.core.type.TypeReference} object
   * @param <T> a T class
   * @return a T object
   */
  public static <T> T convert(Object obj, TypeReference<T> tTypeReference) {
    return objectMapper.convertValue(obj, tTypeReference);
  }
}
