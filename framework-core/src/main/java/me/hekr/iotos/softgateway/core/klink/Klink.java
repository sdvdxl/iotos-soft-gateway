package me.hekr.iotos.softgateway.core.klink;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
/**
 * <p>Klink class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class Klink implements Serializable {
  private static AtomicInteger msgIdCounter = new AtomicInteger();
  @JsonIgnore private boolean msgIdSet;

  /**
   * <p>getNextMsgId.</p>
   *
   * @return a int.
   */
  public static int getNextMsgId() {
    return msgIdCounter.accumulateAndGet(
        1,
        (a, b) -> {
          int r = a + b;
          if (r == Integer.MAX_VALUE) {
            r = 0;
          }
          return r;
        });
  }

  /**
   * <p>getCurMsgId.</p>
   *
   * @return a int.
   */
  public static int getCurMsgId() {
    return msgIdCounter.get();
  }

  /** Constant <code>CMD="cmd"</code> */
  @JsonIgnore public static final String CMD = "cmd";
  private static final long serialVersionUID = -4341021820638489039L;
  protected String action;
  protected long msgId;

  /**
   * <p>Setter for the field <code>msgId</code>.</p>
   *
   * @param msgId a long.
   */
  public void setMsgId(long msgId) {
    this.msgId = msgId;
    msgIdSet = true;
  }

  /** 定制前置机使用，发送原始数据 */
  protected String sysCustomRaw;

  /**
   * <p>setNewMsgId.</p>
   */
  public void setNewMsgId() {
    if (!msgIdSet) {
      msgId = getNextMsgId();
      msgIdSet = true;
    }
  }
}
