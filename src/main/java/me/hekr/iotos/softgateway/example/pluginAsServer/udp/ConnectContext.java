package me.hekr.iotos.softgateway.example.pluginAsServer.udp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Data;
import org.tio.core.Node;

@Data
public class ConnectContext {
  private static final ConcurrentMap<String, ConnectContext> CONTEXT_CONCURRENT_MAP =
      new ConcurrentHashMap<>();
  /** {pk}@{devId} */
  private String id;

  private String pk;
  private String devId;
  private Node remoteInfo;
  private String sessionId;

  public static ConnectContext getByPkAndDevId(String pk, String devId) {
    return CONTEXT_CONCURRENT_MAP.get(getIdByPkAndDevId(pk, devId));
  }

  public static void create(String pk, String devId, Node node, String sessionId) {
    ConnectContext connectContext = new ConnectContext();
    connectContext.setId(getIdByPkAndDevId(pk, devId));
    connectContext.setPk(pk);
    connectContext.setDevId(devId);
    connectContext.setRemoteInfo(node);
    connectContext.setSessionId(sessionId);
    CONTEXT_CONCURRENT_MAP.put(connectContext.id, connectContext);
  }

  public static String getIdByPkAndDevId(String pk, String devId) {
    return pk + "@" + devId;
  }
}
