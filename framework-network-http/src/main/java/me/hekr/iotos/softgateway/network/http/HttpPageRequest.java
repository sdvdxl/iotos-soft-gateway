package me.hekr.iotos.softgateway.network.http;

/** @author iotos */
public abstract class HttpPageRequest extends HttpRequest {
  private Builder builder;

  public HttpPageRequest(Builder builder) {
    super(builder);
  }

  public abstract HttpPageRequest build();
}
