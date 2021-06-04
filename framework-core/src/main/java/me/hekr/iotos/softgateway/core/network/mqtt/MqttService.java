package me.hekr.iotos.softgateway.core.network.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.common.utils.ThreadPoolUtil;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.config.MqttConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.AddTopo;
import me.hekr.iotos.softgateway.core.klink.DevLogin;
import me.hekr.iotos.softgateway.core.klink.DevLogout;
import me.hekr.iotos.softgateway.core.klink.KlinkDev;
import me.hekr.iotos.softgateway.core.klink.Register;
import me.hekr.iotos.softgateway.core.listener.MqttConnectedListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * connect方法用来将数据平台软网关连接至IoTOS
 *
 * @author iotos
 */
@Slf4j
@Component
public class MqttService {
  private final Object registerLock = new Object();

  private final Object addTopoLock = new Object();

  private static final int MAX_RETRY_COUNT = 3;
  private final IotOsConfig iotOsConfig;
  private final BlockingQueue<KlinkDev> queue = new ArrayBlockingQueue<>(1000);
  private final BlockingQueue<KlinkDev> registerQueue = new ArrayBlockingQueue<>(1000);
  private final BlockingQueue<KlinkDev> addTopoQueue = new ArrayBlockingQueue<>(1000);

  @SuppressWarnings("all")
  private final ExecutorService publishExecutor =
      Executors.newSingleThreadExecutor(ThreadUtil.newNamedThreadFactory("publishExecutor", false));

  @SuppressWarnings("all")
  private final ExecutorService connectExecutor =
      Executors.newSingleThreadExecutor(ThreadUtil.newNamedThreadFactory("connectExecutor", false));

  private final AtomicInteger connectCount = new AtomicInteger();
  private volatile long lastConnectTime = 0;
  @Autowired private List<MqttConnectedListener> mqttConnectedListeners;
  private MqttClient client;
  private MqttConnectOptions options;
  @Lazy @Autowired private MqttCallBackImpl mqttCallBackImpl;

  @Autowired
  public MqttService(IotOsConfig iotOsConfig) throws MqttException {
    this.iotOsConfig = iotOsConfig;
    initConfig(iotOsConfig);
    publishExecutor.execute(this::startPublishTask);
    ThreadPoolUtil.DEFAULT_SCHEDULED.scheduleAtFixedRate(
        () -> {
          checkAndLogQueueSize(registerQueue, 2, "register");
          checkAndLogQueueSize(addTopoQueue, 2, "register");
          checkAndLogQueueSize(registerQueue, iotOsConfig.getKlinkQueueSize(), "klink");
        },
        0,
        3,
        TimeUnit.SECONDS);
  }

  private void checkAndLogQueueSize(Queue<?> queue, int threadhole, String type) {
    int size = queue.size();
    if (log.isDebugEnabled()) {
      log.debug(type + " 队列还有 {} 个", size);
    }

    if (size > threadhole) {
      log.warn(type + " 队列未及时消费，还有: {} 个记录", size);
    }
  }

  public  void noticeAddTopoSuccess() {
    synchronized (addTopoLock) {
      addTopoQueue.poll();
      addTopoLock.notifyAll();
    }
  }

  private void initConfig(IotOsConfig iotOsConfig) throws MqttException {
    MqttConfig mqtt = iotOsConfig.getMqttConfig();
    // MemoryPersistence设置clientid的保存形式，默认为以内存保存
    client = new MqttClient(mqtt.getEndpoint(), mqtt.getClientId(), new MemoryPersistence());
    options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setUserName(mqtt.getUsername());
    options.setPassword(mqtt.getPassword());
    // 设置超时时间
    options.setConnectionTimeout(mqtt.getConnectTimeout());
    // 设置会话心跳时间
    options.setKeepAliveInterval(mqtt.getKeepAliveTime());
  }

  public void connect() {
    log.info("软件网关开始连接");

    try {
      client.setCallback(mqttCallBackImpl);
      client.connect(options);
      log.info("软件网关开始连接连接成功！");
      connectCount.incrementAndGet();

      triggerConnectedListeners();
      // 订阅
      client.subscribe(iotOsConfig.getMqttConfig().getSubscribeTopic(), 0);
    } catch (Exception e) {
      log.error("软件网关连接失败！" + e.getMessage(), e);
      if (e instanceof MqttSecurityException) {
        log.error("请检查网关配置pk，devId， devSecret 是否正确；网关设备是否已经创建；连接地址环境是否正确");
      }
      mqttCallBackImpl.triggerConnectFailed();
    }
  }

  private void triggerConnectedListeners() {
    boolean firstConnected = connectCount.get() == 1;
    if (mqttConnectedListeners != null) {
      for (MqttConnectedListener listener : mqttConnectedListeners) {
        if (firstConnected) {
          try {
            listener.onConnected();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        } else {
          try {
            listener.onReConnected();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
      }
    }
  }

  private void closeMqttClient() {
    if (client != null) {
      try {
        client.disconnect();
        client.close(true);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  public void init() {
    connectExecutor.execute(this::loopConnect);
  }

  private void loopConnect() {
    while (isDisconnected()) {
      long diff = System.currentTimeMillis() - lastConnectTime;
      long millis = TimeUnit.SECONDS.toMillis(iotOsConfig.getMqttConfig().getConnectTimeout() + 3);
      long rest = millis - diff;
      if (rest > 0) {
        log.warn("连接频繁，等待 {}s 后重连", TimeUnit.MILLISECONDS.toSeconds(rest));
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
        continue;
      }
      log.info("正在链接...");
      lastConnectTime = System.currentTimeMillis();
      connect();

      if (!isDisconnected()) {
        return;
      }

      try {
        TimeUnit.SECONDS.sleep(iotOsConfig.getMqttConfig().getConnectTimeout() + 3);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void noticeRegisterSuccess() {
    synchronized (registerLock) {
      registerQueue.poll();
      registerLock.notifyAll();
    }
  }

  /** @param klink 消息，发送的时候会被 toJson */
  @SneakyThrows
  public void publish(KlinkDev klink) {

    klink.setNewMsgId();
    if (log.isDebugEnabled()) {
      log.debug("发送 klink: {}", JsonUtil.toJson(klink));
    }

    String pk = klink.getPk();
    String devId = klink.getDevId();
    Optional<DeviceRemoteConfig> byPkAndDevId = DeviceRemoteConfig.getByPkAndDevId(pk, devId);
    // 网关本身不做处理
    if (!iotOsConfig.getGatewayConfig().getPk().equals(pk)) {
      if (!byPkAndDevId.isPresent()) {
        log.warn("没找到设备，pk:{}, devId:{}", pk, devId);
        return;
      }
      DeviceRemoteConfig dev = byPkAndDevId.get();
      if (klink instanceof DevLogin && dev.isOnline()) {
        log.info("设备已经是在线状态 {}", dev);
        return;
      }

      if (klink instanceof DevLogout && dev.isOffline()) {
        log.info("设备已经是离线状态 {}", dev);
        return;
      }
    }
    // 如果是注册设备，确保设备注册成功
    if (klink instanceof Register || Action.REGISTER == Action.of(klink.getAction())) {
      registerQueue.add(klink);
    } else if (klink instanceof AddTopo || Action.ADD_TOPO == Action.of(klink.getAction())) {
      addTopoQueue.add(klink);
    } else {
      queue.add(klink);
    }
  }

  public boolean isDisconnected() {
    return !client.isConnected();
  }

  private void startPublishTask() {
    while (!Thread.currentThread().isInterrupted()) {
      if (isDisconnected()) {
        try {
          TimeUnit.SECONDS.sleep(1);
          continue;
        } catch (InterruptedException ignored) {
        }
      }
      KlinkDev msg = null;
      int waitTime = iotOsConfig.getMqttConfig().getConnectTimeout() * 1000;

      // 优先发送注册信息
      while (!registerQueue.isEmpty()) {
        synchronized (registerLock) {
          msg = registerQueue.peek();
          if (msg != null) {
            try {
              doPublish(msg);
              registerLock.wait(waitTime);
            } catch (MqttException e) {
              log.error(e.getMessage());
              ThreadUtil.sleep(1000);
            } catch (InterruptedException e) {
              return;
            }
          }
        }
      } // 然后发送添加拓扑信息
      while (!addTopoQueue.isEmpty()) {
        synchronized (addTopoLock) {
          msg = addTopoQueue.peek();
          if (msg != null) {
            try {
              doPublish(msg);
              addTopoLock.wait(waitTime);
            } catch (MqttException e) {
              log.error(e.getMessage());
              ThreadUtil.sleep(1000);
            } catch (InterruptedException e) {
              return;
            }
          }
        }
      } // 然后发送添加拓扑信息
      try {
        msg = queue.poll(1, TimeUnit.SECONDS);
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
        continue;
      }

      if (msg != null) {
        trySend(msg);
      }
    }
  }

  private void trySend(KlinkDev klink) {
    String pk = klink.getPk();
    String devId = klink.getDevId();
    // 报错就重试
    for (int i = 0; i < MAX_RETRY_COUNT; i++) {
      try {
        doPublish(klink);

        // 发送成功，如果是发送在线，则设置为在线
        // 网关本身不做处理
        if (!iotOsConfig.getGatewayConfig().getPk().equals(pk)) {
          DeviceRemoteConfig dev = DeviceRemoteConfig.getByPkAndDevId(pk, devId).get();

          if (klink instanceof DevLogin) {
            dev.setOnline();
            return;
          }

          if (klink instanceof DevLogout && dev.isOffline()) {
            dev.setOffline();
            return;
          }
        }
        break;
      } catch (MqttException e) {
        log.error("mqtt发布报错：" + e.getMessage() + ",第 " + (i + 1) + "次重试", e);
        ThreadUtil.sleep(1000);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void doPublish(Object message) throws MqttException {
    if (log.isDebugEnabled()) {
      log.debug("发送消息：{}", JsonUtil.toJson(message));
    }

    client.publish(
        iotOsConfig.getMqttConfig().getPublishTopic(), new MqttMessage(JsonUtil.toBytes(message)));
    if (log.isTraceEnabled()) {
      log.trace("发送消息成功：{}", JsonUtil.toJson(message));
    }
  }

  public void close() {
    closeMqttClient();
    try {
      publishExecutor.shutdown();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
