package me.hekr.iotos.softgateway.core.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.exception.IllegalRemoteConfigException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author iotos
 */
@Slf4j
public class DeviceRemoteConfig implements Serializable {
  private static final Set<DeviceRemoteConfig> SET = new ConcurrentHashSet<>();
  /** 自定义属性 */
  private final Map<Object, Object> customData = new ConcurrentHashMap<>();

  private Map<String, Object> data = new HashMap<>();
  /** 在线状态 */
  private volatile boolean online;

  /** 设备参数 */
  @Getter private final Map<String, Object> modelParams = new ConcurrentHashMap<>();

  /** 是否是网关标识符，true 是网关否则是子设备 */
  @Setter @Getter private boolean gateway;

  public DeviceRemoteConfig() {}

  public DeviceRemoteConfig(Map<String, Object> data) {
    this.data = data;
  }

  public static void parseAndUpdate(String line) {
    update(parse(line));
    log.info("after parseAndAdd: {}", getAllSubDevices());
  }

  @SuppressWarnings("unchecked")
  public static DeviceRemoteConfig parse(String line) {
    Map<String, Object> map;
    try {
      map = JsonUtil.fromJson(line, Map.class);
    } catch (Exception e) {
      throw new IllegalRemoteConfigException("远程配置不是合法的json，" + e.getMessage());
    }
    DeviceRemoteConfig m = new DeviceRemoteConfig(map);
    if (StringUtils.isAnyBlank(m.getPk(), m.getDevId())) {
      throw new IllegalRemoteConfigException("远程配置pk和devId要填写完整");
    }
    return new DeviceRemoteConfig(map);
  }

  public static Set<DeviceRemoteConfig> parseMultiLines(String remoteConfig) {
    return Arrays.stream(remoteConfig.split("\n"))
        .filter(StringUtils::isNotBlank)
        .map(DeviceRemoteConfig::parse)
        .collect(Collectors.<DeviceRemoteConfig>toSet());
  }

  public static void update(DeviceRemoteConfig d) {
    synchronized (SET) {
      remove(d);
      add(d);
      log.info("after update: {}", getStatus());
    }
  }

  private static void add(DeviceRemoteConfig d) {
    Objects.requireNonNull(d, "deviceRemoteConfig is null");
    Objects.requireNonNull(d.getPk(), "pk is null");
    Objects.requireNonNull(d.getDevId(), "devId is null");

    SET.add(d);
    log.info("after add: {}", getStatus());
  }

  public static void remove(DeviceRemoteConfig d) {
    SET.remove(d);
    log.info("after remove: {}", getStatus());
  }

  /**
   * 更新所有数据。
   *
   * <p>清空原来所有数据，添加新的数据
   *
   * @param deviceRemoteConfigs 新的数据
   */
  public static void updateAll(Collection<DeviceRemoteConfig> deviceRemoteConfigs) {
    synchronized (SET) {
      for (DeviceRemoteConfig d : deviceRemoteConfigs) {
        update(d);
      }
    }

    log.info("after updateAll, {}", getStatus());
  }

  public static String getStatus() {
    return "size: " + size() + ", devices: " + getAllSubDevices();
  }

  /**
   * 获取所有子设备（不包含网关）
   *
   * @return
   */
  public static Set<DeviceRemoteConfig> getAllSubDevices() {
    return SET.stream().filter(d -> !d.isGateway()).collect(Collectors.toSet());
  }

  public static Set<DeviceRemoteConfig> getAll() {
    return new HashSet<>(SET);
  }

  public static DeviceRemoteConfig getGatewayDevice() {
    return SET.stream().filter(d -> !d.isGateway()).findAny().get();
  }

  /**
   * 根据子系统的属性来获取唯一的设备
   *
   * @param p 属性
   * @return 匹配属性的一个设备
   */
  public static Optional<DeviceRemoteConfig> getBySubSystemProperties(Props p) {
    return getAllSubDevices().stream().filter(d -> dataEq(d.data, p.data)).findAny();
  }

  static boolean dataEq(Map<String, Object> data, Map<String, Object> properties) {
    return data.entrySet().containsAll(properties.entrySet());
  }

  public static int size() {
    return getAllSubDevices().size();
  }

  public static Optional<DeviceRemoteConfig> getByPkAndDevId(String pk, String devId) {
    return getAll().stream()
        .filter(d -> d.getPk().equals(pk))
        .filter(e -> e.getDevId().equals(devId))
        .findAny();
  }

  public static void parseMultiLinesAndUpdateAll(String content) {
    Set<DeviceRemoteConfig> deviceRemoteConfigs = DeviceRemoteConfig.parseMultiLines(content);
    DeviceRemoteConfig.updateAll(deviceRemoteConfigs);
    log.info("after parseMultiLinesAndUpdateAll: {}", getStatus());
  }

  public static boolean isEmpty() {
    return getAllSubDevices().isEmpty();
  }

  public static void clear() {
    SET.clear();
  }

  /**
   * 增加自定义属性信息
   *
   * @param key key
   * @param val val
   * @return val
   */
  public Object putCustom(Object key, Object val) {
    return this.customData.put(key, val);
  }

  /**
   * 获取自定义信息
   *
   * @param key key
   * @return val
   */
  public Object getCustom(Object key) {
    return this.customData.get(key);
  }

  /**
   * 删除自定义信息
   *
   * @param key key
   * @return 删除的值
   */
  public Object removeCustom(Object key) {
    return this.customData.remove(key);
  }

  public void setOnline() {
    online = true;
  }

  public void setOffline() {
    online = false;
  }

  public boolean isOffline() {
    return !isOnline();
  }

  public boolean isOnline() {
    return online;
  }

  @Override
  public String toString() {
    return data.toString();
  }

  @SuppressWarnings("unchecked")
  public <T> T getProp(String prop) {
    return (T) data.get(prop);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DeviceRemoteConfig)) {
      return false;
    }
    DeviceRemoteConfig that = (DeviceRemoteConfig) o;
    return getPk().equals(that.getPk()) && getDevId().equals(that.getDevId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPk(), getDevId());
  }

  /**
   * 获取 pk
   *
   * @return pk
   */
  public String getPk() {
    return getProp("pk");
  }

  /**
   * 设置 pk
   *
   * @param pk pk
   * @return DeviceRemoteConfig
   */
  public DeviceRemoteConfig setPk(String pk) {
    data.put("pk", pk);
    return this;
  }

  /**
   * 获取 devId
   *
   * @return devId
   */
  public String getDevId() {
    return getProp("devId");
  }

  /**
   * 设置 devId
   *
   * @param devId devId
   * @return DeviceRemoteConfig
   */
  public DeviceRemoteConfig setDevId(String devId) {
    data.put("devId", devId);
    return this;
  }

  /**
   * 获取设备名字 devName
   *
   * @return 设备名字
   */
  public String getDevName() {
    return getProp("devName");
  }

  /**
   * 获取设备类型，可以为 null
   *
   * @return 设备类型
   */
  public String getDeviceType() {
    return getProp("deviceType");
  }

  /**
   * 更新本地参数
   *
   * @param params 新参数
   * @return true 发生了变化； false 没有变化
   */
  public boolean updateDeviceParams(Map<String, Object> params) {
    if (CollectionUtil.isEmpty(params)) {
      return false;
    }

    boolean notChanged =
        params.entrySet().stream()
            .filter(e -> Objects.nonNull(e.getKey()))
            .filter(e -> Objects.nonNull(e.getValue()))
            .allMatch(e -> e.getValue().equals(modelParams.get(e.getKey())));

    modelParams.putAll(params);
    return !notChanged;
  }

  @JsonIgnoreType
  public static class Props implements Serializable {
    private final Map<String, Object> data = new HashMap<>();

    public static PropsBuilder p(String prop, Object value) {
      PropsBuilder pb = new PropsBuilder();
      pb.props = new Props();
      pb.put(prop, value);
      return pb;
    }

    private void put(String prop, Object value) {
      data.put(prop, value);
    }

    @Override
    public String toString() {
      return data.toString();
    }

    public static class PropsBuilder {
      private Props props;

      public PropsBuilder put(String prop, Object value) {
        props.put(prop, value);
        return this;
      }

      public Props get() {
        return props;
      }
    }
  }
}
