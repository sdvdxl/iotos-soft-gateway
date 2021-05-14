package me.hekr.iotos.softgateway.core.klink.processor;

import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.Klink;

/** @author iotos */
public interface Processor<T extends Klink> {

  /**
   * 处理对应的action的数据
   *
   * <p>如果处理失败（code不等于0），则对应的pk和devId都会被设置成reqPk和reqDevId。比如：子设备不存在，将错误信息记录到网关上，可以追溯消息记录。
   *
   * <p>如果处理成功，并且klinkResp，主动设置了pk和devId，则不会被重置为req信息，并且 deviceRequest 的 pk和devId也会设置为klinkResp中的信息。
   *
   * @param klink klink json object 格式数据 如：{"action":"devSend","msgId":1,xx:xx}
   */
  void handle(T klink);

  /**
   * 对应的action
   *
   * @return Action
   */
  Action getAction();
}
