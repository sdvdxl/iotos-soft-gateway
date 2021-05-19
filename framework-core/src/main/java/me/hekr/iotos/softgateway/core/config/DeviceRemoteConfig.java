package me.hekr.iotos.softgateway.core.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

/** @author iotos */
@Slf4j
public class DeviceRemoteConfig implements Serializable {
  private static final Set<DeviceRemoteConfig> SET = new ConcurrentHashSet<>();
  private Map<String, Object> data = new HashMap<>();
  /** 自定义属性  */
  private final Map<Object, Object> customData = new ConcurrentHashMap<>();

  /** 在线状态 */
  private volatile boolean online;

  private DeviceRemoteConfig() {}

  private DeviceRemoteConfig(Map<String, Object> data) {
    this.data = data;
  }

  public static void parseAndAdd(String line) {
    @SuppressWarnings("unchecked")
    Map<String, Object> map = JsonUtil.fromJson(line, Map.class);
    DeviceRemoteConfig m = new DeviceRemoteConfig(map);
    parseAndAdd(m);
    log.info("after parseAndAdd: {}", getAll());
  }

  /**
   * 增加自定义属性信息
   * @param key key
   * @param val val
   * @return val
   */
  public Object putCustom(Object key, Object val){
    return this.customData.put(key,val);
  }

  /**
   * 获取自定义信息
   * @param key key
   * @return val
   */
  public Object getCustom(Object key){
    return this.customData.get(key);
  }

  /**
   * 删除自定义信息
   * @param key key
   * @return 删除的值
   */
  public Object removeCustom(Object key){
    return this.customData.remove(key);
  }

  @SuppressWarnings("unchecked")
  public static DeviceRemoteConfig parse(String line) {
    return new DeviceRemoteConfig(JsonUtil.fromJson(line, Map.class));
  }

  public static Set<DeviceRemoteConfig> parseMultiLines(String remoteConfig) {
    return Arrays.stream(remoteConfig.split("\n"))
        .filter(StringUtils::isNotBlank)
        .map(DeviceRemoteConfig::parse)
        .collect(Collectors.<DeviceRemoteConfig>toSet());
  }

  public static void parseAndAdd(DeviceRemoteConfig d) {
    SET.add(d);
    log.info("after parseAndAdd: {}", getStatus());
  }

  public static void remove(DeviceRemoteConfig d) {
    SET.remove(d);
    log.info("after remove: {}", getStatus());
  }

  /**
   * 更新所有数据。
   *
   * <p>删除参数中不存在的，更新都存在的数据
   *
   * @param deviceRemoteConfigs 新的数据
   */
  public static void updateAll(Collection<DeviceRemoteConfig> deviceRemoteConfigs) {
    SET.removeAll(CollectionUtil.subtract(SET, deviceRemoteConfigs));
    addAll(deviceRemoteConfigs);
    log.info("after updateAll, {}", getStatus());
  }

  public static String getStatus() {
    return "size: " + size() + ", devices: " + getAll();
  }

  /**
   * 添加新的集合数据
   *
   * @param deviceRemoteConfigs 新的数据
   */
  private static void addAll(Collection<DeviceRemoteConfig> deviceRemoteConfigs) {
    SET.addAll(deviceRemoteConfigs);
    log.info("after addAll: {}", getStatus());
  }

  public static Set<DeviceRemoteConfig> getAll() {
    return SET;
  }

  /**
   * 根据子系统的属性来获取唯一的设备
   *
   * @param p 属性
   * @return 匹配属性的一个设备
   */
  public static Optional<DeviceRemoteConfig> getBySubSystemProperties(Props p) {
    return getAll().stream().filter(d -> dataEq(d.data, p.data)).findAny();
  }

  private static boolean dataEq(Map<String, Object> data, Map<String, Object> properties) {
    return data.entrySet().containsAll(properties.entrySet());
  }

  public static int size() {
    return getAll().size();
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
    return getAll().isEmpty();
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

  public String getPk() {
    return getProp("pk");
  }

  public String getDevId() {
    return getProp("devId");
  }

  public String getDevName() {
    return getProp("devName");
  }

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
