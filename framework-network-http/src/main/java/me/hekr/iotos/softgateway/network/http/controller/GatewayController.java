package me.hekr.iotos.softgateway.network.http.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.dto.CacheDeviceKey;
import me.hekr.iotos.softgateway.core.exception.BizException;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author du
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController {
  @Autowired private KlinkService klinkService;

  /**
   * 获取所有子设备信息
   *
   * @return 远程配置设备集合
   */
  @GetMapping("/getAllSubDevices")
  public Set<DeviceRemoteConfig> getAllSubDevices() {
    return DeviceRemoteConfig.getAllSubDevices();
  }

  /**
   * 获取所有缓存的参数信息
   *
   * @return 缓存参数集合
   */
  @GetMapping("/getAllCacheParam")
  public List<JSONObject> getAllCacheParam() {
    return klinkService.getCahceValue().entrySet().stream()
        .map(
            e ->
                new JSONObject()
                    .set("pk", e.getKey().getPk())
                    .set("devId", e.getKey().getDevId())
                    .set("param", e.getKey().getParam())
                    .set("cmd", e.getKey().getCmd())
                    .set("value", e.getValue()))
        .collect(Collectors.toList());
  }

  /** 清空所有缓存 */
  @DeleteMapping("/invalidAllCache")
  public void invalidAllCache() {
    klinkService.invalidAllCache();
  }

  /**
   * 清空指定参数缓存
   *
   * @param key 缓存key
   */
  @DeleteMapping("/invalidCache")
  public void invalidCache(@RequestBody CacheDeviceKey key) {
    checkDeviceExists(key);
    klinkService.invalidCache(key);
  }

  private static void checkDeviceExists(CacheDeviceKey key) {
    if (DeviceRemoteConfig.getByPkAndDevId(key.getPk(), key.getDevId())
        .orElseThrow(() -> new BizException("设备不存在"))
        .isGateway()) {
      throw new BizException("不能操作网关");
    }
  }

  /**
   * 清空指定设备缓存
   *
   * @param key key
   */
  @DeleteMapping("/invalidCacheByDevice")
  public void invalidCacheByDevice(@RequestBody CacheDeviceKey key) {
    checkDeviceExists(key);
    klinkService
        .CACHE_PARAM_VALUE
        .asMap()
        .forEach(
            (cacheKey, value) -> {
              if (cacheKey.getPk().equals(key.getPk())
                  && cacheKey.getDevId().equals(key.getDevId())) {
                klinkService.invalidCache(cacheKey);
              }
            });
  }

  /** 发送登录事件（所有子设备） */
  @PutMapping("/deviceSendLoginAll")
  public void deviceSendLoginAll() {
    for (DeviceRemoteConfig device : DeviceRemoteConfig.getAllSubDevices()) {
      klinkService.devLogin(device.getPk(), device.getDevId(), "手动触发");
      ThreadUtil.safeSleep(10);
    }
  }

  /**
   * 指定设备发送登录事件
   *
   * @param key key（pk,devId）
   */
  @PutMapping("/deviceSendLogin")
  public void deviceSendLogin(@RequestBody CacheDeviceKey key) {
    checkDeviceExists(key);
    klinkService.devLogin(key.getPk(), key.getDevId(), "手动触发");
  }

  /** 发送登出事件(所有子设备) */
  @PutMapping("/deviceSendLogoutAll")
  public void deviceSendLogoutAll() {
    for (DeviceRemoteConfig device : DeviceRemoteConfig.getAllSubDevices()) {
      klinkService.devLogout(device.getPk(), device.getDevId(), "手动触发");
      ThreadUtil.safeSleep(10);
    }
  }

  /**
   * 指定设备发送登出事件
   *
   * @param key （pk，devId）
   */
  @PutMapping("/deviceSendLogout")
  public void deviceSendLogout(@RequestBody CacheDeviceKey key) {
    checkDeviceExists(key);
    klinkService.devLogout(key.getPk(), key.getDevId(), "手动触发");
  }
}
