package me.hekr.iotos.softgateway.core.common.codec;

import me.hekr.iotos.softgateway.core.common.klink.KlinkDev;

/** 原始数据与klink数据转换抽象接口 */
public interface DataCodec {

  KlinkDev decode(Object data);

  Object encode(KlinkDev klink);
}