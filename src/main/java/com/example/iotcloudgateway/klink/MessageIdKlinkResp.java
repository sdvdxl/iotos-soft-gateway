package com.example.iotcloudgateway.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class MessageIdKlinkResp extends KlinkResp {

  private static final long serialVersionUID = 6100726877614668403L;
  protected String messageId;
}