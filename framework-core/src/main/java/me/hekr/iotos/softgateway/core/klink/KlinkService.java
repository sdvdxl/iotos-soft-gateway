package me.hekr.iotos.softgateway.core.klink;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.ParseUtil;
import me.hekr.iotos.softgateway.common.utils.ThreadPoolUtil;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig.Props;
import me.hekr.iotos.softgateway.core.config.GatewayConfig;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.constant.Constants;
import me.hekr.iotos.softgateway.core.dto.CacheDeviceKey;
import me.hekr.iotos.softgateway.core.dto.DeviceMapper;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 这是发送消息的服务端 配置数据平台相关参数，可执行设备上报数据至平台以及平台下发数据至设备 Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * 此为模拟烟雾传感器连接至智慧消防平台，并且其与IoTOS平台进行交互的代码 添加设备和配置流程详情请见《设备连接软网关数据上报说明书》
 *
 * @author iotos
 * @version $Id: $Id
 */
@SuppressWarnings("ALL")
@Slf4j
@Service
@Getter
public class KlinkService {
  /** Constant <code>sleepMills=200</code> */
  public static volatile long sleepMills = 200;

  private final IotOsConfig iotOsConfig;
  private final MqttService mqttService;
  public final Cache<CacheDeviceKey, Object> CACHE_PARAM_VALUE;

  /**
   * Constructor for KlinkService.
   *
   * @param iotOsConfig a {@link IotOsConfig} object.
   * @param mqttService a {@link me.hekr.iotos.softgateway.core.network.mqtt.MqttService} object.
   */
  public KlinkService(IotOsConfig iotOsConfig, @Lazy MqttService mqttService) {
    this.iotOsConfig = iotOsConfig;
    this.mqttService = mqttService;
    CACHE_PARAM_VALUE =
        Caffeine.newBuilder()
            .maximumSize(iotOsConfig.getCacheParamsSize())
            .expireAfterWrite(iotOsConfig.getCacheExpireSeconds(), TimeUnit.SECONDS)
            .recordStats()
            .build();
    ThreadPoolUtil.DEFAULT_SCHEDULED.scheduleAtFixedRate(
        () -> {
          log.info("缓存命中统计：{}", CACHE_PARAM_VALUE.stats());
        },
        0,
        60,
        TimeUnit.SECONDS);
  }

  /**
   * 动态注册设备
   *
   * @param pk pk
   * @param devId devId
   * @param devName 设备名字 可以为空
   */
  @SneakyThrows
  public void register(String pk, String devId, String devName) {
    register(pk, devId, null, devName);
  }

  /**
   * 动态注册设备
   *
   * @param pk pk
   * @param devId pk
   * @param productSecret 产品密钥
   * @param devName 设备名字 可以为空
   */
  @SneakyThrows
  public void register(String pk, String devId, String productSecret, String devName) {
    doRegister(pk, devId, productSecret, devName);
  }

  private void doRegister(String pk, String devId, String productSecret, String devName)
      throws Exception {
    Register register = new Register();
    register.setNewMsgId();
    register.setDevId(devId);
    register.setPk(pk);
    register.setName(devName);
    if (productSecret != null) {
      register.setRandom(Constants.RANDOM);
      register.setHashMethod(ParseUtil.HASH_METHOD);
      register.setSign(
          Hex.encodeHexString(
              ParseUtil.hmacSHA1Encrypt(pk + productSecret + Constants.RANDOM, productSecret)));
    }

    mqttService.publish(register);
  }

  /**
   * 注册设备并添加拓扑
   *
   * <p>设备名字默认为空
   *
   * @param pk pk
   * @param devId devId
   */
  @SneakyThrows
  public void addDev(String pk, String devId) {
    addDev(pk, devId, null);
  }

  /**
   * 注册设备并添加拓扑关系
   *
   * @param pk pk
   * @param devId devId
   * @param devName 设备名字，可以为空
   */
  @SneakyThrows
  public void addDev(String pk, String devId, String devName) {
    addDev(pk, null, devId, null, devName);
  }

  /**
   * 添加拓扑
   *
   * @param pk pk
   * @param devId devId
   */
  @SneakyThrows
  public void addTopo(String pk, String devId) {
    doAddTopo(pk, devId, null);
  }

  /**
   * 注册设备并添加拓扑关系
   *
   * @param pk pk
   * @param productSecret 产品密钥
   * @param devId devId
   * @param devSecret 设备密钥
   * @param devName 设备名字
   */
  @SneakyThrows
  public void addDev(
      String pk, String productSecret, String devId, String devSecret, String devName) {
    register(pk, devId, productSecret, devName);
    doAddTopo(pk, devId, devSecret);
  }

  private void doAddTopo(String pk, String devId, String subDevSecret) throws Exception {
    AddTopo addTopo = new AddTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(pk);
    topoSub.setDevId(devId);
    if (subDevSecret != null) {
      topoSub.setHashMethod(ParseUtil.HASH_METHOD);
      topoSub.setRandom(Constants.RANDOM);
      topoSub.setSign(
          Hex.encodeHexString(
              ParseUtil.hmacSHA1Encrypt(
                  pk + devId + subDevSecret + Constants.RANDOM, subDevSecret)));
    }
    addTopo.setSub(topoSub);
    GatewayConfig g = iotOsConfig.getGatewayConfig();
    addTopo.setPk(g.getPk());
    addTopo.setDevId(g.getDevId());
    mqttService.publish(addTopo);
  }

  /**
   * 设备上线
   *
   * @param pk pk
   * @param devId devId
   */
  @SneakyThrows
  public void devLogin(String pk, String devId) {
    doDevLogin(pk, devId, null);
  }

  /**
   * 设备上线
   *
   * @param pk pk
   * @param devId devId
   * @param raw 原始数据
   */
  @SneakyThrows
  public void devLogin(String pk, String devId, String raw) {
    doDevLogin(pk, devId, raw);
  }

  private void doDevLogin(String pk, String devId, String raw) {
    DevLogin devLogin = new DevLogin();
    devLogin.setDevId(devId);
    devLogin.setPk(pk);
    devLogin.setSysCustomRaw(raw);
    mqttService.publish(devLogin);
  }

  /**
   * 设备离线
   *
   * @param pk pk
   * @param devId devId
   */
  @SneakyThrows
  public void devLogout(String pk, String devId) {
    doDevLogout(pk, devId, null);
  }

  /**
   * 设备离线
   *
   * @param pk pk
   * @param devId devId
   * @param raw 原始数据
   */
  @SneakyThrows
  public void devLogout(String pk, String devId, String raw) {
    doDevLogout(pk, devId, raw);
  }

  private void doDevLogout(String pk, String devId, String raw) {
    DevLogout devLogout = new DevLogout();
    devLogout.setDevId(devId);
    devLogout.setPk(pk);
    devLogout.setSysCustomRaw(raw);
    mqttService.publish(devLogout);
  }

  /** 获取拓扑关系 */
  @SneakyThrows
  public void getTopo() {
    doGetTopo();
  }

  private void doGetTopo() {
    GetTopo getTopo = new GetTopo();
    getTopo.setPk(iotOsConfig.getGatewayConfig().getPk());
    getTopo.setDevId(iotOsConfig.getGatewayConfig().getDevId());
    mqttService.publish(getTopo);
  }

  /**
   * 删除子设备拓扑关系
   *
   * @param pk pk
   * @param devId devId
   */
  @SneakyThrows
  public void delDev(String pk, String devId) {
    DelTopo delTopo = new DelTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(pk);
    topoSub.setDevId(devId);
    delTopo.setSub(topoSub);
    GatewayConfig g = iotOsConfig.getGatewayConfig();
    delTopo.setPk(g.getPk());
    delTopo.setDevId(g.getDevId());
    mqttService.publish(delTopo);
  }

  /**
   * 设备发送数据
   *
   * @param pk pk
   * @param devId devId
   * @param cmd 命令
   * @param params 参数
   */
  @SneakyThrows
  public void devSend(String pk, String devId, String cmd, Map<String, Object> params) {
    ModelData modelData = new ModelData();
    modelData.setCmd(cmd);
    modelData.setParams(params);
    devSend(pk, devId, modelData);
  }

  /**
   * 设备发送数据
   *
   * <p>如果找不到 mapper，则打印日志，不会真实发送数据
   *
   * @param mapper 设备
   * @param klink klink
   */
  @SneakyThrows
  public void sendKlink(DeviceMapper mapper, KlinkDev klink) {
    Optional<DeviceRemoteConfig> devMapper = getDeviceMapper(mapper);
    if (!devMapper.isPresent()) {
      return;
    }
    DeviceRemoteConfig dev = devMapper.get();
    klink.setPk(dev.getPk());
    klink.setDevId(dev.getDevId());
    mqttService.publish(klink);
  }

  /**
   * 发送数据，比较底层的发送方法，需要自己构造 klink
   *
   * @param klink klink
   */
  @SneakyThrows
  public void sendKlink(KlinkDev klink) {
    mqttService.publish(klink);
  }

  /**
   * 编码为hex
   *
   * @param bytes 字节数组
   * @return 编码字符串
   */
  public String bytesToHex(byte[] bytes) {
    return Hex.encodeHexString(bytes);
  }

  /**
   * 编码为String（utf8）
   *
   * @param bytes 字节数组
   * @return 编码字符串
   */
  public String bytesToString(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }

  /**
   * 编码为base64
   *
   * @param bytes 字节数组
   * @return 编码字符串
   */
  public String bytesToBase64(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }

  /**
   * 设备发送数据， 没有参数，只有命令
   *
   * @param pk pk
   * @param devId devId
   * @param cmd 命令
   */
  @SneakyThrows
  public void devSend(String pk, String devId, String cmd) {
    devSend(pk, devId, cmd, null);
  }

  /**
   * 设备发送数据。 该方式会将 params 拆成多条，分别发送，cmd 命令是同一个
   *
   * <p>使用默认缓存配置
   *
   * @param pk pk
   * @param devId devId
   * @param data 物模型数据
   */
  public void devSendWithCache(String pk, String devId, ModelData data) {
    for (Map.Entry<String, Object> entry : data.getParams().entrySet()) {
      String param = entry.getKey();
      Object value = entry.getValue();
      CacheDeviceKey cacheDeviceKey = CacheDeviceKey.of(pk, devId, param);
      Object cacheValue = CACHE_PARAM_VALUE.getIfPresent(cacheDeviceKey);
      String cacheValueStr = cacheValue == null ? null : String.valueOf(cacheValue);
      String newValueStr = String.valueOf(value);
      if (cacheValue != null && Objects.equals(cacheValue, newValueStr)) {
        log.debug("命中缓存，不发送参数。 pk: {}, devId: {}, param: {}, value: {}", pk, devId, param, value);
        continue;
      }

      DevSend kLink = new DevSend();
      kLink.setPk(pk);
      kLink.setDevId(devId);
      kLink.setData(ModelData.cmd(data.getCmd()).param(param, value));
      mqttService.publish(kLink);
      if (value != null) {
        CACHE_PARAM_VALUE.put(cacheDeviceKey, value);
      }
    }
  }

  /**
   * 设备发送数据
   *
   * @param pk pk
   * @param devId devId
   * @param data 物模型数据
   */
  public void devSend(String pk, String devId, ModelData data) {
    DevSend kLink = new DevSend();
    kLink.setPk(pk);
    kLink.setDevId(devId);
    kLink.setData(data);
    mqttService.publish(kLink);
  }

  /**
   * 设备发送数据
   *
   * @param pk pk
   * @param devId devId
   * @param data 物模型数据
   * @param raw 原始数据
   */
  public void devSend(String pk, String devId, ModelData data, String raw) {
    DevSend kLink = new DevSend();
    kLink.setPk(pk);
    kLink.setDevId(devId);
    kLink.setData(data);
    kLink.setSysCustomRaw(raw);
    mqttService.publish(kLink);
  }

  /** 获取远程配置文件 */
  @SneakyThrows
  public void getConfig() {
    GetConfig getConfig = new GetConfig();
    GatewayConfig g = iotOsConfig.getGatewayConfig();
    getConfig.setPk(g.getPk());
    getConfig.setDevId(g.getDevId());
    mqttService.publish(getConfig);
  }

  /**
   * 发送数据
   *
   * <p>如果找不到 mapper，则打印日志，不会真实发送数据
   *
   * @param mapper 设备映射
   * @param data 物模型数据
   */
  public void devSend(DeviceMapper mapper, ModelData data) {
    devSend(mapper, data, null);
  }

  /**
   * 发送数据
   *
   * <p>如果找不到 mapper，则打印日志，不会真实发送数据
   *
   * @param mapper 设备映射
   * @param data 物模型数据
   * @param raw 原始数据
   */
  public void devSend(DeviceMapper mapper, ModelData data, String raw) {
    Optional<DeviceRemoteConfig> devMapper = getDeviceMapper(mapper);
    if (!devMapper.isPresent()) {
      return;
    }
    DeviceRemoteConfig dev = devMapper.get();
    devSend(dev.getPk(), dev.getDevId(), data, raw);
  }

  /**
   * 设备登录
   *
   * @param mapper 设备
   */
  public void devLogin(DeviceMapper mapper) {
    Optional<DeviceRemoteConfig> devMapper = getDeviceMapper(mapper);
    if (!devMapper.isPresent()) {
      return;
    }
    DeviceRemoteConfig dev = devMapper.get();

    devLogin(dev.getPk(), dev.getDevId());
  }

  /**
   * 设备登出
   *
   * <p>如果找不到 mapper，则打印日志，不会真实发送数据
   *
   * @param mapper 设备
   */
  public void devLogout(DeviceMapper mapper) {
    Optional<DeviceRemoteConfig> devMapper = getDeviceMapper(mapper);
    if (!devMapper.isPresent()) {
      return;
    }
    DeviceRemoteConfig dev = devMapper.get();
    devLogout(dev.getPk(), dev.getDevId());
  }

  /**
   * 获取设备映射
   *
   * @param mapper 设备关系
   * @return 设备配置
   */
  private Optional<DeviceRemoteConfig> getDeviceMapper(DeviceMapper mapper) {
    Props props = mapper.getProps();
    Objects.requireNonNull(props, "mapper.getProps 必须不为 null");
    Optional<DeviceRemoteConfig> subsystemDev = DeviceRemoteConfig.getBySubSystemProperties(props);
    if (!subsystemDev.isPresent()) {
      log.debug("没有配置映射设备信息，请在远程配置中进行配置，设备信息：{}", props);
    }

    return subsystemDev;
  }

  /**
   * 发送控制设备回复
   *
   * @param mapper 设备
   * @param code 错误码 0 成功； 错误定义其他，200以上
   * @param desc 错误描述
   */
  public void sendCloudSendResp(DeviceMapper mapper, int code, String desc) {
    KlinkResp resp = new KlinkResp();
    resp.setCode(code);
    resp.setDesc(desc);
    resp.setAction(Action.CLOUD_SEND_RESP.getAction());
    sendKlink(mapper, resp);
  }

  /**
   * 发送控制设备回复Success
   *
   * @param pk pk
   * @param devId devId
   */
  public void sendCloudSendRespOK(String pk, String devId) {
    KlinkResp resp = new KlinkResp();
    resp.setPk(pk);
    resp.setDevId(devId);
    resp.setErrorCode(ErrorCode.SUCCESS);
    resp.setAction(Action.CLOUD_SEND_RESP.getAction());
    sendKlink(resp);
  }

  /**
   * 发送控制设备回复
   *
   * @param pk pk
   * @param devId devId
   * @param code 错误码 0 成功； 错误定义其他，200以上
   * @param desc 错误描述
   */
  public void sendCloudSendResp(String pk, String devId, int code, String desc) {
    KlinkResp resp = new KlinkResp();
    resp.setPk(pk);
    resp.setDevId(devId);
    resp.setCode(code);
    resp.setDesc(desc);
    resp.setAction(Action.CLOUD_SEND_RESP.getAction());
    sendKlink(resp);
  }

  /**
   * 发送控制设备回复
   *
   * @param mapper 设备
   * @param errorCode 错误码
   */
  public void sendCloudSendResp(DeviceMapper mapper, ErrorCode errorCode) {
    sendCloudSendResp(mapper, errorCode.getCode(), errorCode.getDesc());
  }

  /**
   * 失效缓存
   *
   * @param key 缓存key
   */
  public void invalidCache(CacheDeviceKey key) {
    CACHE_PARAM_VALUE.invalidate(key);
  }

  /** 失效所有缓存 */
  public void invalidAllCache() {
    CACHE_PARAM_VALUE.invalidateAll();
  }

  /**
   * 获取所有缓存中的值
   *
   * <p>注意并不保证值存在，因为缓存过期时间是根据实际情况设置的
   *
   * @return 缓存中所有的参数信息
   */
  public Map<CacheDeviceKey, Object> getCahceValue() {
    return CACHE_PARAM_VALUE.asMap();
  }

  /**
   * 强刷缓存的参数到iotos
   *
   * @param key 缓存key
   * @return true: 缓存存在 false:缓存不存在
   */
  public boolean flushCacheToCloud(CacheDeviceKey key) {
    Object cacheValue = CACHE_PARAM_VALUE.getIfPresent(key);
    if (cacheValue == null) {
      return false;
    }

    devSend(
        key.getPk(), key.getDevId(), ModelData.cmd(key.getCmd()).param(key.getParam(), cacheValue));
    return true;
  }

  /**
   * 强刷缓存的参数到iotos
   *
   * @param pk pk
   * @param devId devId
   * @return 刷新参数个数
   */
  public int flushCacheToCloud(String pk, String devId) {
    List<Map.Entry<CacheDeviceKey, Object>> list =
        CACHE_PARAM_VALUE.asMap().entrySet().stream()
            .filter(e -> e.getKey().equalsDev(pk, devId))
            .collect(Collectors.toList());
    for (Map.Entry<CacheDeviceKey, Object> entry : list) {
      CacheDeviceKey key = entry.getKey();
      devSend(
          key.getPk(),
          key.getDevId(),
          ModelData.cmd(key.getCmd()).param(key.getParam(), entry.getValue()));
    }
    return list.size();
  }

  /**
   * 强刷缓存的参数到iotos
   *
   * @return 刷新参数个数
   */
  public int flushCacheToCloud() {
    ConcurrentMap<CacheDeviceKey, Object> map = CACHE_PARAM_VALUE.asMap();
    for (Map.Entry<CacheDeviceKey, Object> entry : map.entrySet()) {
      CacheDeviceKey key = entry.getKey();
      devSend(
          key.getPk(),
          key.getDevId(),
          ModelData.cmd(key.getCmd()).param(key.getParam(), entry.getValue()));
    }

    return map.size();
  }
}
