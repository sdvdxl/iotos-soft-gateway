package hekr.me.iotos.softgateway.northProxy.device;

import lombok.Getter;
import org.tio.utils.jfinal.P;

@Getter
public enum DeviceType {
  /** 道闸 */
  BARRIER(P.get("subdev.barrier.pk"), P.get("subdev.barrier.productSecret")),
  ;
  private final String pk;
  private final String prodSecret;

  DeviceType(String pk, String prodSecret) {
    this.pk = pk;
    this.prodSecret = prodSecret;
  }
}
