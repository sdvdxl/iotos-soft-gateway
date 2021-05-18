package me.hekr.iotos.softgateway.network.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.http.MediaType;

/** @author iotos */
public class HttpRequest {
  protected Request request;
  String baseUrl;
  private Builder requestBuilder;

  public HttpRequest(Builder builder) {
    this.requestBuilder = builder;
  }

  public HttpRequest(Request request) {
    this.request = request;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Request getOkHttpRequest() {
    HttpUrl httpUrl = HttpUrl.parse(requestBuilder.path);
    // 为 null，非完整的 url，拼接 baseUrl
    if (httpUrl == null) {
      httpUrl = HttpUrl.parse(baseUrl + requestBuilder.path);
      Objects.requireNonNull(httpUrl, "需要填写baseUrl 或者 request 的 path 需要填写完整的 url 地址");
    }
    HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
    requestBuilder.queryParams.forEach(
        (k, v) -> httpUrlBuilder.addQueryParameter(k, String.valueOf(v)));

    return requestBuilder.okHttpRequestBuilder.url(httpUrlBuilder.build()).build();
  }

  public static class Builder {
    private final Map<String, Object> queryParams = new HashMap<>(10);
    private final Headers.Builder headerBuilder = new Headers.Builder();
    Request.Builder okHttpRequestBuilder;
    private byte[] body;
    private HttpMethod method = HttpMethod.GET;
    private MediaType mediaType = MediaType.APPLICATION_JSON;
    private String path = "";

    public HttpRequest build() {
      okHttpRequestBuilder = new Request.Builder();
      handleHeaders();
      handleMethodAndBody();
      return new HttpRequest(this);
    }

    private void handleUrl() {
      HttpUrl.Builder httpUrlBuilder = HttpUrl.get(path).newBuilder();
      queryParams.forEach((k, v) -> httpUrlBuilder.addQueryParameter(k, String.valueOf(v)));
      okHttpRequestBuilder.url(httpUrlBuilder.build());
    }

    private void handleMethodAndBody() {
      RequestBody requestBody = null;
      if (okhttp3.internal.http.HttpMethod.requiresRequestBody(method.name())) {
        requestBody = RequestBody.create(okhttp3.MediaType.parse(mediaType.toString()), body);
      }
      okHttpRequestBuilder.method(method.name(), requestBody);
    }

    private void handleHeaders() {
      okHttpRequestBuilder.headers(headerBuilder.build());
    }

    public Builder addHeader(String name, String value) {
      headerBuilder.add(name, value);
      return this;
    }

    public Builder body(byte[] body) {
      this.body = body;
      return this;
    }

    public Builder addParam(String name, Object value) {
      queryParams.put(name, value);
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder method(HttpMethod method) {
      this.method = method;
      return this;
    }

    public Builder mediaType(MediaType mediaType) {
      this.mediaType = mediaType;
      return this;
    }
  }
}
