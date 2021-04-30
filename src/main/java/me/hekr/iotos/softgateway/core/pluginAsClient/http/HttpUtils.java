package me.hekr.iotos.softgateway.core.pluginAsClient.http;

import cn.hutool.http.HttpUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * 此类实现了Http常用的几种请求方式，包括get、post以及data-form格式的post请求
 *
 * <p>同时此类还提供downloadFile(String url)方法可以下载指定url的文件
 */
@Slf4j
public class HttpUtils {
  public OkHttpClient HttpClient;

  /** 初始化Http client,进行相关配置 */
  public HttpUtils() {
    this.HttpClient =
        new OkHttpClient.Builder()
            // 设置链接超时
            .connectTimeout(10, TimeUnit.SECONDS)
            // 设置写数据超时
            .writeTimeout(10, TimeUnit.SECONDS)
            // 设置读数据超时
            .readTimeout(30, TimeUnit.SECONDS)
            .build(); // 创建OkHttpClient对象
  }

  /**
   * post请求
   *
   * @param url
   * @param headerParams
   * @param bodyParams
   * @return
   * @throws RuntimeException
   */
  @SneakyThrows
  public byte[] post(String url, Map<String, String> headerParams, Map<String, Object> bodyParams)
      throws RuntimeException {

    HttpUrl.Builder httpBuider = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();

    Request.Builder requestBuider =
        new Request.Builder().url(httpBuider.build()).headers(Headers.of(headerParams));

    // 此处为body为json格式的post请求
    MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");

    Response response = null;
    try {
      response = this.HttpClient.newCall(requestBuider.build()).execute();
      return response.body().bytes();
    } catch (IOException e) {
      log.warn(e.getMessage());
    } finally {
      IOUtils.closeQuietly(response);
    }
    throw new RuntimeException();
  }

  /** form-data 形式的post请求 */
  public byte[] postFormData(
      String url, Map<String, String> headerParams, Map<String, Object> bodyParams)
      throws RuntimeException {

    HttpUrl.Builder httpBuider = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();

    Request.Builder requestBuider =
        new Request.Builder().url(httpBuider.build()).headers(Headers.of(headerParams));

    // 此处为body为json格式的post请求
    MediaType mediaType = MultipartBody.FORM;
    Builder builder = new Builder().setType(mediaType);
    bodyParams.forEach(
        (s, o) -> {
          if (o instanceof String) {
            builder.addFormDataPart(s, o.toString());
          } else {
            builder.addFormDataPart("file", s, RequestBody.create(mediaType, (byte[]) o));
          }
        });

    requestBuider.post(builder.build());
    Response response = null;
    try {
      response = this.HttpClient.newCall(requestBuider.build()).execute();
      return response.body().bytes();
    } catch (IOException e) {
      log.warn(e.getMessage());
    } finally {
      IOUtils.closeQuietly(response);
    }
    throw new RuntimeException();
  }

  /**
   * get请求
   *
   * @param url
   * @param headerParams
   * @param params
   * @return
   */
  public byte[] get(String url, Map<String, String> headerParams, Map<String, Object> params) {
    // 设置HTTP请求参数
    url += getParams(params);
    Headers setHeaders = SetHeaders(headerParams);
    Request request = new Request.Builder().url(url).headers(setHeaders).build();
    Response response = null;
    try {
      response = this.HttpClient.newCall(request).execute();
      return response.body().bytes();
    } catch (Exception e) {
      log.warn(e.getMessage());
    } finally {
      IOUtils.closeQuietly(response);
    }
    throw new RuntimeException();
  }

  /*编辑参数列表*/
  @SneakyThrows
  public static String getParams(Map<String, Object> params) {
    StringBuilder sb = new StringBuilder("?");
    if (params != null && !params.isEmpty()) {
      for (Map.Entry<String, Object> item : params.entrySet()) {
        Object value = item.getValue();
        if (value != null) {
          sb.append("&");
          sb.append(URLEncoder.encode(item.getKey(), "UTF-8"));
          sb.append("=");
          sb.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
        }
      }
      return sb.toString();
    } else {
      return "";
    }
  }

  /*编辑header*/
  public static Headers SetHeaders(Map<String, String> headersParams) {
    Headers headers;
    Headers.Builder headersbuilder = new Headers.Builder();
    if (headersParams != null && !headersParams.isEmpty()) {
      Iterator<String> iterator = headersParams.keySet().iterator();
      String key = "";
      while (iterator.hasNext()) {
        key = iterator.next();
        headersbuilder.add(key, headersParams.get(key));
      }
    }
    headers = headersbuilder.build();
    return headers;
  }

  /** 下载工具 */
  public static byte[] downloadFile(String url) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      HttpUtil.download(url, outputStream, false);
      return outputStream.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(outputStream);
    }
    throw new RuntimeException();
  }
}
