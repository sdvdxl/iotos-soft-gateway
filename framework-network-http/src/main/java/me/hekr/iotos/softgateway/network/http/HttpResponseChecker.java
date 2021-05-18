package me.hekr.iotos.softgateway.network.http;

/**
 * 是否成功校验
 *
 * @author iotos
 */
public interface HttpResponseChecker {

  /** 默认实现， http status 200 &lt;=code&lt;300 */
  HttpResponseChecker DEFAULT =
      new HttpResponseChecker() {
        @Override
        public boolean isSuccess(HttpResponse response) {
          return response.response.isSuccessful();
        }

        @Override
        public String desc() {
          return "status code 不是2xx";
        }
      };
  /**
   * 是否成功
   *
   * @param response response
   * @return 成功 true，否则 false
   */
  boolean isSuccess(HttpResponse response);

  /**
   * 校验失败描述
   *
   * @return 描述
   */
  default String desc() {
    return "response 校验失败";
  }
  ;
}
