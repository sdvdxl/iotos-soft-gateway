package me.hekr.iotos.softgateway.network.http;

import lombok.Getter;
import okhttp3.Response;

/** @author IoTOS */
public class HttpResponse {
  Response response;
  @Getter boolean success;
  @Getter byte[] bytes;
  @Getter int statusCode;

  public HttpResponse(Response response, byte[] bytes) {
    this.response = response;
    this.statusCode = response.code();
    this.bytes = bytes;
  }

  public HttpResponse() {}
}
