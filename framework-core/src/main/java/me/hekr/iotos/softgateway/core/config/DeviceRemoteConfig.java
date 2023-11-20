package me.hekr.iotos.softgateway.core.config;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.dto.DeviceMapper;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.util.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * DeviceRemoteConfig class.
 *
 * @author iotos
 * @version $Id: $Id
 */
@Slf4j
@Data
public class DeviceRemoteConfig implements Serializable {
  /** Constant <code>PROPERTY_PK="pk"</code> */
  public static final String PROPERTY_PK = "pk";

  /** Constant <code>PROPERTY_DEVID="devId"</code> */
  public static final String PROPERTY_DEVID = "devId";

  /** Constant <code>PROPERTY_DEVICE_NAME="devName"</code> */
  public static final String PROPERTY_DEVICE_NAME = "devName";

  /** Constant <code>PROPERTY_DEVICE_TYPE="deviceType"</code> */
  public static final String PROPERTY_DEVICE_TYPE = "deviceType";

  private static final Set<DeviceRemoteConfig> SET = new ConcurrentHashSet<>();

  /** 自定义属性 */
  private final Map<Object, Object> customData = new ConcurrentHashMap<>();

  private Map<String, Object> data = new HashMap<>();

  /** 在线状态 */
  private volatile boolean online;

  /** 是否是网关标识符，true 是网关否则是子设备 */
  @Setter @Getter private boolean gateway;

  /** Constructor for DeviceRemoteConfig. */
  public DeviceRemoteConfig() {}

  /**
   * Constructor for DeviceRemoteConfig.
   *
   * @param data a {@link java.util.Map} object.
   */
  public DeviceRemoteConfig(Map<String, Object> data) {
    this.data = data;
  }

  /**
   * parse.
   *
   * @param line a {@link java.lang.String} object.
   * @return a {@link me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig} object.
   */
  @SuppressWarnings("unchecked")
  public static DeviceRemoteConfig parse(String line) {
    Map<String, Object> map;
    try {
      map = JsonUtil.fromJson(line, Map.class);
    } catch (Exception e) {
      String msg = "解析远程配置失败, line:" + line + ", error:" + e.getMessage();
      log.error(msg);
      ImmutableMap<String, Object> params = ImmutableMap.of("desc", msg);
      try {
        KlinkService klinkService = SpringContextUtils.getBean(KlinkService.class);
        IotOsConfig iotOsConfig = SpringContextUtils.getBean(IotOsConfig.class);
        klinkService.devSend(
            iotOsConfig.getGatewayConfig().getPk(),
            iotOsConfig.getGatewayConfig().getDevId(),
            "reportError",
            params);
      } catch (Exception ex) {
        log.error(e.getMessage(), e);
      }
      return null;
    }
    DeviceRemoteConfig m = new DeviceRemoteConfig(map);
    if (StringUtils.isAnyBlank(m.getPk(), m.getDevId())) {
      String msg = "远程配置pk和devId要填写完整, line: " + line;
      log.error(msg);
      ImmutableMap<String, Object> params = ImmutableMap.of("desc", msg);
      try {
        KlinkService klinkService = SpringContextUtils.getBean(KlinkService.class);
        IotOsConfig iotOsConfig = SpringContextUtils.getBean(IotOsConfig.class);

        klinkService.devSend(
            iotOsConfig.getGatewayConfig().getPk(),
            iotOsConfig.getGatewayConfig().getDevId(),
            "reportError",
            params);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      return null;
    }
    return new DeviceRemoteConfig(map);
  }

  /**
   * parseMultiLines.
   *
   * @param remoteConfig a {@link java.lang.String} object.
   * @return a {@link java.util.Set} object.
   */
  public static Set<DeviceRemoteConfig> parseMultiLines(String remoteConfig) {
    return Arrays.stream(remoteConfig.split("\n"))
        .filter(StringUtils::isNotBlank)
        .map(DeviceRemoteConfig::parse)
        .filter(Objects::nonNull)
        .collect(Collectors.<DeviceRemoteConfig>toSet());
  }

  private static void add(DeviceRemoteConfig d) {
    Objects.requireNonNull(d, "deviceRemoteConfig is null");
    Objects.requireNonNull(d.getPk(), "pk is null");
    Objects.requireNonNull(d.getDevId(), "devId is null");
    SET.add(d);
  }

  /**
   * remove.
   *
   * @param p a {@link me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig.Props} object.
   */
  public static void remove(Props p) {
    SET.removeIf(d -> p.data.equals(d.data));
    SET.remove(p);
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
      SET.clear();
      SET.addAll(deviceRemoteConfigs);
    }

    log.info("after updateAll, {}", getStatus());
  }

  /**
   * getStatus.
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getStatus() {
    return "deviceRemoteConfig size: " + size();
  }

  /**
   * 获取所有子设备（不包含网关）
   *
   * @return 所有子设备集合
   */
  public static Set<DeviceRemoteConfig> getAllSubDevices() {
    return SET.stream().filter(d -> !d.isGateway()).collect(Collectors.toSet());
  }

  /**
   * getAll.
   *
   * @return a {@link java.util.Set} object.
   */
  public static Set<DeviceRemoteConfig> getAll() {
    return new HashSet<>(SET);
  }

  /**
   * 获取网关设备
   *
   * @return 网关设备
   */
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
    return getAll().stream().filter(d -> dataEq(d.data, p.data)).findAny();
  }

  /**
   * 判断两个map是否相等
   *
   * @param data 待比较的map
   * @param properties 待比较的map
   * @return 是否相等
   */
  static boolean dataEq(Map<String, Object> data, Map<String, Object> properties) {
    return data.entrySet().containsAll(properties.entrySet());
  }

  /**
   * 获取所有子设备数量
   *
   * @return 数量
   */
  public static int size() {
    return getAllSubDevices().size();
  }

  /**
   * 根据pk和devId获取设备
   *
   * @param pk pk
   * @param devId devId
   * @return 设备
   */
  public static Optional<DeviceRemoteConfig> getByPkAndDevId(String pk, String devId) {
    return getBySubSystemProperties(Props.p(PROPERTY_PK, pk).put(PROPERTY_DEVID, devId).get());
  }

  /**
   * 根据设备映射器获取设备
   *
   * @param deviceMapper 设备映射器
   * @return 设备
   */
  public static Optional<DeviceRemoteConfig> getByDeviceMapper(DeviceMapper deviceMapper) {
    return getBySubSystemProperties(deviceMapper.getProps());
  }

  /**
   * 解析多行配置，并更新所有设备
   *
   * @param content 多行配置
   */
  public static void parseMultiLinesAndUpdateAll(String content) {
    Set<DeviceRemoteConfig> deviceRemoteConfigs = DeviceRemoteConfig.parseMultiLines(content);
    DeviceRemoteConfig.updateAll(deviceRemoteConfigs);
    log.info("after parseMultiLinesAndUpdateAll: {}", getStatus());
  }

  /**
   * 判断是有子设备
   *
   * @return 有子设备返回true，否则返回false
   */
  public static boolean isEmpty() {
    return getAllSubDevices().isEmpty();
  }

  /** 清空所有设备 */
  public static void clear() {
    SET.clear();
  }

  /**
   * 根据 pk 和 devId 更新设备
   *
   * @param deviceRemoteConfig 设备
   */
  public static synchronized void updateByPkAndDevId(DeviceRemoteConfig deviceRemoteConfig) {
    removeByPkAndDevId(deviceRemoteConfig.getPk(), deviceRemoteConfig.getDevId());
    add(deviceRemoteConfig);
  }

  /**
   * 根据 pk 和 devId 删除设备
   *
   * @param pk pk
   * @param devId devId
   */
  private static void removeByPkAndDevId(String pk, String devId) {
    SET.removeIf(d -> d.getPk().equals(pk) && d.getDevId().equals(devId));
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

  /** Setter for the field <code>online</code>. */
  public void setOnline() {
    online = true;
  }

  /** setOffline. */
  public void setOffline() {
    online = false;
  }

  /**
   * isOffline.
   *
   * @return a boolean.
   */
  public boolean isOffline() {
    return !isOnline();
  }

  /**
   * isOnline.
   *
   * @return a boolean.
   */
  public boolean isOnline() {
    return online;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return data.toString();
  }

  /**
   * getProp.
   *
   * @param prop a {@link java.lang.String} object.
   * @param <T> a T object.
   * @return a T object.
   */
  @SuppressWarnings("unchecked")
  public <T> T getProp(String prop) {
    return (T) data.get(prop);
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
    data.put(PROPERTY_PK, pk);
    return this;
  }

  /**
   * 获取 devId
   *
   * @return devId
   */
  public String getDevId() {
    return getProp(PROPERTY_DEVID);
  }

  /**
   * 设置 devId
   *
   * @param devId devId
   * @return DeviceRemoteConfig
   */
  public DeviceRemoteConfig setDevId(String devId) {
    data.put(PROPERTY_DEVID, devId);
    return this;
  }

  /**
   * 获取设备名字 devName
   *
   * @return 设备名字
   */
  public String getDevName() {
    return getProp(PROPERTY_DEVICE_NAME);
  }

  /**
   * 获取设备类型，可以为 null
   *
   * @return 设备类型
   */
  public String getDeviceType() {
    return getProp(PROPERTY_DEVICE_TYPE);
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
