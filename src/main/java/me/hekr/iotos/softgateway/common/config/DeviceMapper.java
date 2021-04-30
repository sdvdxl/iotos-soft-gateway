package me.hekr.iotos.softgateway.common.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import me.hekr.iotos.softgateway.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

/** @author iotos */
public class DeviceMapper {
  private static final Set<DeviceMapper> SET = new ConcurrentHashSet<>();
  private Map<String, Object> data = new HashMap<>();

  private DeviceMapper() {}

  private DeviceMapper(Map<String, Object> data) {
    this.data = data;
  }

  public static void parseAndAdd(String line) {
    Map<String, Object> map = JsonUtil.fromJson(line, Map.class);
    DeviceMapper m = new DeviceMapper(map);
    parseAndAdd(m);
  }

  public static DeviceMapper parse(String line) {
    return new DeviceMapper(JsonUtil.fromJson(line, Map.class));
  }

  public static Set<DeviceMapper> parseMultiLines(String remoteConfig) {
    return Arrays.stream(remoteConfig.split("\n"))
        .filter(StringUtils::isNotBlank)
        .map(DeviceMapper::parse)
        .collect(Collectors.<DeviceMapper>toSet());
  }

  public static void parseAndAdd(DeviceMapper d) {
    SET.add(d);
  }

  public static void remove(DeviceMapper d) {
    SET.remove(d);
  }

  /**
   * 更新所有数据。
   *
   * <p>删除参数中不存在的，更新都存在的数据
   *
   * @param deviceMappers 新的数据
   */
  public static void updateAll(Collection<DeviceMapper> deviceMappers) {
    SET.removeAll(CollectionUtil.subtract(SET, deviceMappers));
    addAll(deviceMappers);
  }

  /**
   * 添加新的集合数据
   *
   * @param deviceMappers 新的数据
   */
  private static void addAll(Collection<DeviceMapper> deviceMappers) {
    SET.addAll(deviceMappers);
  }

  public static Set<DeviceMapper> getAll() {
    return SET;
  }

  /**
   * 根据子系统的属性来获取唯一的设备
   *
   * @param p 属性
   * @return 匹配属性的一个设备
   */
  public static Optional<DeviceMapper> getBySubSystemProperties(Props p) {
    return getAll().stream().filter(d -> dataEq(d.data, p.data)).findAny();
  }

  private static boolean dataEq(Map<String, Object> data, Map<String, Object> properties) {
    return data.entrySet().containsAll(properties.entrySet());
  }

  public static int size() {
    return getAll().size();
  }

  public static Optional<DeviceMapper> getByPkAndDevId(String pk, String devId) {
    return getAll().stream()
        .filter(d -> d.getPk().equals(pk))
        .filter(e -> e.getDevId().equals(devId))
        .findAny();
  }

  public static void parseMultiLinesAndUpdateAll(String content) {
    DeviceMapper.updateAll(DeviceMapper.parseMultiLines(content));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DeviceMapper)) {
      return false;
    }
    DeviceMapper that = (DeviceMapper) o;
    return getPk().equals(that.getPk()) && getDevId().equals(that.getDevId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPk(), getDevId());
  }

  public String getPk() {
    return (String) data.get("pk");
  }

  public String getDevId() {
    return (String) data.get("devId");
  }

  public static class Props {
    private final Map<String, Object> data = new HashMap<String, Object>();

    public static PropsBuilder p(String prop, Object value) {
      PropsBuilder pb = new PropsBuilder();
      pb.props = new Props();
      pb.put(prop, value);
      return pb;
    }

    private void put(String prop, Object value) {
      data.put(prop, value);
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
