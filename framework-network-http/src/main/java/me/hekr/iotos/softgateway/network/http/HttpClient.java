package me.hekr.iotos.softgateway.network.http;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

/** @author du */
@Data
@Slf4j
public class HttpClient {

  private OkHttpClient okHttpClient;
  private String baseUrl;
  /** http response 结果校验，如果不通过则抛出异常 */
  @Setter private HttpResponseChecker httpResponseChecker = HttpResponseChecker.DEFAULT;

  @Setter private HttpExceptionHandler httpExceptionHandler = HttpExceptionHandler.THROW_HANDLER;

  private HttpClient() {}

  @SneakyThrows
  public static HttpClient newInstance(String baseUrl, int timeoutOfSecs, Level level) {
    ConnectionPool connectionPool = new ConnectionPool(1, 30, TimeUnit.SECONDS);
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(level);
    OkHttpClient client =
        new OkHttpClient()
            .newBuilder()
            // 读超时
            .readTimeout(Duration.ofSeconds(timeoutOfSecs))
            // 写超时
            .writeTimeout(Duration.ofSeconds(timeoutOfSecs))
            //
            .callTimeout(Duration.ofSeconds(timeoutOfSecs))
            // 连接池
            .connectionPool(connectionPool)
            // 连接超时
            .connectTimeout(Duration.ofSeconds(3))
            // 不重试
            .retryOnConnectionFailure(false)
            .addInterceptor(interceptor)
            .build();
    HttpClient httpClient = new HttpClient();
    httpClient.okHttpClient = client;
    httpClient.baseUrl = baseUrl;
    return httpClient;
  }

  @SneakyThrows
  public static HttpClient newInstance(String url) {
    return newInstance(url, 3, Level.BASIC);
  }

  @SneakyThrows
  public HttpResponse exec(HttpRequest request) {
    HttpResponse httpResponse;
      request.baseUrl = baseUrl;
    try {
      Response response = okHttpClient.newCall(request.getOkHttpRequest()).execute();
      ResponseBody body = response.body();
      byte[] bytes = body == null ? null : body.bytes();
      httpResponse = new HttpResponse(response, bytes);
      httpResponse.success = httpResponseChecker.isSuccess(httpResponse);
    } catch (Exception e) {
      httpResponse = new HttpResponse();
      httpExceptionHandler.onException(request, e);
    }
    return httpResponse;
  }

  @SneakyThrows
  public <T> T exec(HttpRequest request, Class<T> clazz) {
    HttpResponse response = exec(request);
    return JsonUtil.fromBytes(response.bytes, clazz);
  }

  /**
   * @param request 请求
   * @param parser response解析
   * @param curPage 初始化当前页码
   * @param pageSize 初始化每页大小
   * @param <R> response
   * @param <T> response 中的元素
   * @return 元素列表
   */
  public <R, T> List<T> exec(
      HttpRequestPageable<R> request, ResponseParser parser, int curPage, int pageSize) {
    List<T> resultList = new ArrayList<>();
    boolean hasMore = true;
    int page = curPage;
    while (hasMore) {
      HttpResponse resp = exec(request.buildPageRequest(page, pageSize));
      HttpPageResponse<R, T> parse = parser.parse(resp);
      resultList.addAll(parse.getItems());
      hasMore = request.hasMore(parse.getResult());
      page++;
    }

    return resultList;
  }
}
