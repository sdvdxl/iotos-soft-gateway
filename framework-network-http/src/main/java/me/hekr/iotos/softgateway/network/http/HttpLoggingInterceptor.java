package me.hekr.iotos.softgateway.network.http;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class HttpLoggingInterceptor implements Interceptor {

  private final Level level;
  private String clientName;

  public HttpLoggingInterceptor(String clientName, Level level) {
    this.clientName = clientName;
    this.level = level;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    if (level != null) {
      switch (level) {
        case NONE:
          break;
        case BODY:
        case HEADERS:
          log.info(
              "clientName: {}, url: {}, headers: {}", clientName, request.url(), request.headers());
        default:
      }
    }
    return chain.proceed(request);
  }

  public enum Level {
    NONE,
    HEADERS,
    BODY
  }
}
