package hekr.me.iotos.softgateway.common.codec;

import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.common.klink.KlinkDev;
import org.tio.core.ChannelContext;

/** 原始数据与klink数据转换抽象接口 */
public interface DataCodec {

  KlinkDev decode(Object data);

  Object encode(KlinkDev klink);
}
