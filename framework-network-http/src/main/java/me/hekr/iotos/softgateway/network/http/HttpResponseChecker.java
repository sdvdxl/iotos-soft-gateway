package me.hekr.iotos.softgateway.network.http;

/**
 * 是否成功校验
 *
 * @author iotos
 */
public interface HttpResponseChecker {

  /** 默认实现， http status 200<=code<300 */
  HttpResponseChecker DEFAULT =
      new HttpResponseChecker() {
        @Override
        public boolean isSuccess(HttpResponse response) {
          return response.response.isSuccessful();
        }
      };
  /**
   * 是否成功
   *
   * @param response response
   * @return 成功 true，否则 false
   */
  boolean isSuccess(HttpResponse response);
}
