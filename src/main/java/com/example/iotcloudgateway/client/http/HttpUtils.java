package com.example.iotcloudgateway.client.http;

import com.alibaba.fastjson.JSON;
import com.example.iotcloudgateway.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * @author ：jiatao
 * @date ：2020/3/9
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
  public Response post(String url, Map<String, String> headerParams, Map<String, Object> bodyParams)
      throws RuntimeException {

    HttpUrl.Builder httpBuider = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();

    Request.Builder requestBuider =
        new Request.Builder().url(httpBuider.build()).headers(Headers.of(headerParams));

    // 此处为body为json格式的post请求
    MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
    RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(bodyParams));
    requestBuider.post(requestBody);

    Response response = null;
    try {
      response = this.HttpClient.newCall(requestBuider.build()).execute();
      return response;
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
  public Response get(String url, Map<String, String> headerParams, Map<String, Object> params) {
    // 设置HTTP请求参数
    url += getParams(params);
    Headers setHeaders = SetHeaders(headerParams);
    Request request = new Request.Builder().url(url).headers(setHeaders).build();
    Response response = null;
    try {
      response = this.HttpClient.newCall(request).execute();
      return response;
    } catch (Exception e) {
      log.warn(e.getMessage());
    } finally {
      IOUtils.closeQuietly(response);
    }
    throw new RuntimeException();
  }

  /*编辑参数列表*/
  public String getParams(Map<String, Object> params) {
    StringBuilder sb = new StringBuilder("?");
    if (params != null && !params.isEmpty()) {
      for (Map.Entry<String, Object> item : params.entrySet()) {
        Object value = item.getValue();
        if (value != null) {
          sb.append("&");
          sb.append(item.getKey());
          sb.append("=");
          sb.append(value);
        }
      }
      return sb.toString();
    } else {
      return "";
    }
  }

  /*编辑header*/
  public Headers SetHeaders(Map<String, String> headersParams) {
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
}
