package me.hekr.iotos.softgateway.common.klink;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dev implements Serializable {

  private static final long serialVersionUID = -3682121569498210336L;
  private String pk;
  private String devId;
}