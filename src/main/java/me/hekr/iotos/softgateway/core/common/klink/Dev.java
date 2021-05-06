package me.hekr.iotos.softgateway.core.common.klink;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"pk", "devId"})
public class Dev implements Serializable {

  private static final long serialVersionUID = -3682121569498210336L;
  private String pk;
  private String devId;
  private String name;

  public Dev(String pk, String devId) {
    this.pk = pk;
    this.devId = devId;
  }

  @Override
  public String toString() {
    return "{pk='" + pk + '\'' + ", devId='" + devId + '\'' + ", name='" + name + "'}";
  }
}
