package me.hekr.iotos.softgateway.sample;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import javax.annotation.PostConstruct;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.network.http.HttpClient;
import me.hekr.iotos.softgateway.network.http.HttpPageResponse;
import me.hekr.iotos.softgateway.network.http.HttpRequest;
import me.hekr.iotos.softgateway.network.http.HttpRequestPageable;
import me.hekr.iotos.softgateway.network.http.HttpResponse;
import me.hekr.iotos.softgateway.network.http.HttpResponseChecker;
import me.hekr.iotos.softgateway.network.http.PageableResponseParser;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 运行前先启动 HttpServerSample
 *
 * @author iotos
 */
@SpringBootApplication
public class HttpClientSample {
  public static void main(String[] args) {
    SpringApplication.run(HttpClientSample.class, args);
  }

  @PostConstruct
  public void init() {
    testRequestPageable();
    testRequestPageableItems();
    testChecker();
  }

  /** 测试分页 */
  public static void testRequestPageable() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/", Level.BODY);
    client.setHttpResponseChecker(
        response -> {
          // 200才算成功
          return response.getStatusCode() == 200;
        });
    HttpRequestPageable<DeviceResponse> request =
        new HttpRequestPageable<DeviceResponse>(0, 10) {
          @Override
          public HttpRequest buildRequest(int curPage, int pageSize) {
            return HttpRequest.builder()
                .addParam("curPage", curPage)
                .addParam("pageSize", pageSize)
                .build();
          }

          @Override
          public boolean hasMore(DeviceResponse resp) {
            return resp.hasMore();
          }
        };
    PageableResponseParser<DeviceResponse, Device> parser =
        r -> HttpPageResponse.wrap(JsonUtil.fromBytes(r.getBytes(), DeviceResponse.class));
    List<Device> list = client.exec(request, parser, 0, 1);
    System.out.println(list);
  }

  /** 测试分页 ，紧紧返回列表的情况 */
  public static void testRequestPageableItems() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/");
    HttpRequestPageable<DeviceResponse> request =
        new HttpRequestPageable<DeviceResponse>(0, 10) {
          @Override
          public HttpRequest buildRequest(int curPage, int pageSize) {
            return HttpRequest.builder()
                .path("items")
                .addParam("curPage", curPage)
                .addParam("pageSize", pageSize)
                .build();
          }

          @Override
          public boolean hasMore(DeviceResponse resp) {
            return resp.hasMore();
          }
        };
    PageableResponseParser<DeviceResponse, Device> parser =
        r -> {
          List<Device> devices =
              JsonUtil.fromBytes(r.getBytes(), new TypeReference<List<Device>>() {});
          return new HttpPageResponse<>(new DeviceResponse(devices));
        };
    List<Device> list = client.exec(request, parser, 0, 1);
    System.out.println(list);
  }

  /** 测试自定义校验 */
  public static void testChecker() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/");
    client.setHttpResponseChecker(
        new HttpResponseChecker() {
          @Override
          public boolean isSuccess(HttpResponse response) {
            return response.getStatusCode() == 201;
          }

          @Override
          public String desc() {
            return "不是201！！";
          }
        });
    client.exec(HttpRequest.builder().path("").build());
  }
}
