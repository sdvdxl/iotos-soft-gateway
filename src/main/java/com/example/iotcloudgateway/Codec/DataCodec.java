package com.example.iotcloudgateway.Codec;

import iot.cloud.os.core.api.dto.klink.Klink;

/** 原始数据与klink数据转换抽象接口 */
public interface DataCodec {

  Klink decode(Object data);

  Object encode(Klink klink);
}
