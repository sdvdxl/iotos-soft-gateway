package me.hekr.iotos.softgateway.network.http;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
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
@Slf4j
public class HttpClient {

  @Getter private final String baseUrl;
  @Getter private OkHttpClient okHttpClient;
  /** http response 结果校验，如果不通过则抛出异常 */
  @Setter private HttpResponseChecker httpResponseChecker = HttpResponseChecker.DEFAULT;

  private HttpClient(String baseUrl, OkHttpClient client) {
    this.baseUrl = baseUrl;
    this.okHttpClient = client;
  }

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
            .connectTimeout(Duration.ofSeconds(timeoutOfSecs))
            // 不重试
            .retryOnConnectionFailure(false)
            .addInterceptor(interceptor)
            .build();
    return new HttpClient(baseUrl, client);
  }

  @SneakyThrows
  public static HttpClient newInstance(String url) {
    return newInstance(url, 3, Level.BASIC);
  }

  @SneakyThrows
  public static HttpClient newInstance(String url, Level level) {
    return newInstance(url, 3, level);
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
    } catch (Exception e) {
      throw new HttpException(request, e);
    }

    boolean success = httpResponseChecker.isSuccess(httpResponse);
    if (!success) {
      throw new HttpException(request, httpResponse, httpResponseChecker.desc());
    }

    return httpResponse;
  }

  /**
   * 执行请求，并自动从 json 对象解析成对象
   *
   * @param request 请求
   * @param clazz 映射的对象
   * @param <T> 参数类型
   * @return 结果
   */
  @SneakyThrows
  public <T> T exec(HttpRequest request, Class<T> clazz) {
    return exec(request, response -> JsonUtil.fromBytes(response.bytes, clazz));
  }

  @SneakyThrows
  public <T> T exec(HttpRequest request, ResponseParser<T> parser) {
    HttpResponse response = exec(request);
    return parser.parse(response);
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
  public <R extends PageableResponse<T>, T> List<T> exec(
      HttpRequestPageable<R> request,
      PageableResponseParser<R, T> parser,
      int curPage,
      int pageSize) {
    List<T> resultList = new ArrayList<>();
    boolean hasMore = true;
    int page = curPage;
    while (hasMore) {
      HttpResponse resp = exec(request.buildRequest(page, pageSize));
      HttpPageResponse<R, T> parse = parser.parse(resp);
      resultList.addAll(parse.getItems());
      hasMore = request.hasMore(parse.getResult());
      page++;
    }

    return resultList;
  }
}
