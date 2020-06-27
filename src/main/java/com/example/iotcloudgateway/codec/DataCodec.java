package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.klink.DevSend;
import org.tio.core.ChannelContext;

/** 原始数据与klink数据转换抽象接口 */
public interface DataCodec {

  DevSend decode(Object data, ChannelContext channelContext);

  Object encode(DevSend klink, ChannelContext channelContext);
}
