package com.example.iotcloudgateway.Codec;

import iot.cloud.os.core.api.dto.klink.KlinkDev;

/** 原始数据与klink数据转换抽象接口 */
public interface DataCodec {

  KlinkDev decode(Object data);

  Object encode(KlinkDev klink);
}
