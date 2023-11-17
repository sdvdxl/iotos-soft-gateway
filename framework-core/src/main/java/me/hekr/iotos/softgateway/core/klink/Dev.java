package me.hekr.iotos.softgateway.core.klink;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
/**
 * <p>Dev class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"pk", "devId"})
public class Dev implements Serializable {

  private static final long serialVersionUID = -3682121569498210336L;
  private String pk;
  private String devId;
  private String name;

  /**
   * <p>Constructor for Dev.</p>
   *
   * @param pk a {@link java.lang.String} object.
   * @param devId a {@link java.lang.String} object.
   */
  public Dev(String pk, String devId) {
    this.pk = pk;
    this.devId = devId;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{pk='" + pk + '\'' + ", devId='" + devId + '\'' + ", name='" + name + "'}";
  }
}
