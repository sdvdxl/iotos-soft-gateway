package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.klink.KlinkDev;
import org.tio.core.ChannelContext;

/** 原始数据与klink数据转换抽象接口 */
public interface DataCodec {

  KlinkDev decode(Object data, ChannelContext channelContext);

  Object encode(KlinkDev klink, ChannelContext channelContext);
}
