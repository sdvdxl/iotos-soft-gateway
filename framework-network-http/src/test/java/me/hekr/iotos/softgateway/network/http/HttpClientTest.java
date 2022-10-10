package me.hekr.iotos.softgateway.network.http;

import me.hekr.iotos.softgateway.network.http.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Test;

public class HttpClientTest {

  @Test
  public void exec() {
    HttpClient httpClient =
        HttpClient.newInstance("test", "https://pie.dev", 300, Level.BODY, false);
    HttpResponse exec = httpClient.exec(HttpRequest.builder().path("/get").build());
    System.out.println(new String(exec.bytes));
  }
}
