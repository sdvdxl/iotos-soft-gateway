package hekr.me.iotcloudgateway.mqtt.processor;

import hekr.me.iotcloudgateway.enums.Action;
import hekr.me.iotcloudgateway.klink.Klink;

/** @author du */
public interface Processor<T extends Klink> {

  /**
   * 处理对应的action的数据
   *
   * <p>如果处理失败（code不等于0），则对应的pk和devId都会被设置成reqPk和reqDevId。比如：子设备不存在，将错误信息记录到网关上，可以追溯消息记录。
   *
   * <p>如果处理成功，并且klinkResp，主动设置了pk和devId，则不会被重置为req信息，并且 deviceRequest 的 pk和devId也会设置为klinkResp中的信息。
   *
   * @param klink klink json object 格式数据 如：{"action":"devSend","msgId":1,xx:xx}
   * @return 如果没有特殊返回消息，可以返回null； 如果要自定义返回消息，则需要返回不为null的实体；其中返回值 outMessage
   *     中的的pk，devId，action，msgId不受自定义返回影响，都是processManager统一处理。
   */
  void handle(T klink);

  /**
   * 对应的action
   *
   * @return Action
   */
  Action getAction();
}
