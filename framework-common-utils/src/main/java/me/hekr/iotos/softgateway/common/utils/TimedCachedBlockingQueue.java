package me.hekr.iotos.softgateway.common.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时， 固定缓存大小的queue。可以实现批量操作
 *
 * <p>没到时间先缓存到Bag里面，Bag 有大小。 如果Bag 满则 打包发送到 Queue。
 *
 * <p>Bag 没满但是有值，到时间也会被发送到Queue中
 *
 * @author du
 */
@Slf4j
public class TimedCachedBlockingQueue<T> {

  private final int queueSize;
  private final String name;
  private ScheduledExecutorService scheduledExecutorService;
  private final BlockingDeque<Bag<T>> bagQueue;
  private final int batchSize;
  private Bag<T> bag;
  /**
   * 总数量为 batchSize * queueSize
   *
   * @param interval 定时周期
   * @param timeUnit 定时单位
   * @param batchSize 批量缓存大小
   * @param queueSize 队列大小
   */
  public TimedCachedBlockingQueue(
      String name, int interval, TimeUnit timeUnit, int batchSize, int queueSize) {
    this.name = name;
    Assert.isTrue(interval > 0, "interval 必须大于0");
    Assert.notNull(timeUnit, "timeUnit 不能为null");
    Assert.isTrue(interval > 0, "batchSize 必须大于0");
    this.batchSize = batchSize;
    this.queueSize = queueSize;
    this.bagQueue = new LinkedBlockingDeque<>(queueSize);
    scheduledExecutorService =
        new ScheduledThreadPoolExecutor(
            1,
            new ThreadFactoryBuilder()
                .setNamePrefix("TimedCachedBlockingQueue-")
                .setDaemon(true)
                .build(),
            new AbortPolicy());
    scheduledExecutorService.scheduleAtFixedRate(
        () -> {
          try {
            checkAndPutBag();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        },
        interval,
        interval,
        timeUnit);
  }

  /** 监测背包，如果不为null，说明有元素，入队 */
  private synchronized void checkAndPutBag() {
    if (bagQueue.size() == queueSize) {
      log.warn("TimedCachedBlockingQueue: {}, 队列满({})，建议加快消费或者加大队列长度", name, queueSize);
    }

    if (log.isDebugEnabled()) {
      log.debug(
          "name: {} check and put bag into queue, current bag: {} , bag size: {}",
          name,
          bagQueue.size() + 1,
          bag == null ? 0 : bag.getSize());
    }
    if (bag != null) {
      boolean offer = bagQueue.offer(bag);
      if (offer) {
        bag = null;
      }
    }
  }

  /**
   * 放入元素（阻塞）
   *
   * @param t
   * @return
   * @throws InterruptedException
   */
  public synchronized TimedCachedBlockingQueue put(T t) throws InterruptedException {
    if (bag == null) {
      bag = new Bag<>(batchSize);
    }

    boolean bagOffer = bag.offer(t);
    boolean isFull = !bagOffer || bag.isFull();
    // 背包满了
    if (isFull) {
      bagQueue.put(bag);
    }

    return this;
  }

  /**
   * 拉取背包（注意该方法不能使用 synchronized， 会和 put 方法发生死锁）
   *
   * @param timeout
   * @param timeUnit
   * @return
   * @throws InterruptedException
   */
  public Bag<T> poll(long timeout, TimeUnit timeUnit) throws InterruptedException {
    Bag<T> tBag = bagQueue.poll(timeout, timeUnit);
    return tBag;
  }

  /**
   * 背包
   *
   * @param <T>
   */
  public static class Bag<T> {
    private final ArrayBlockingQueue<T> bagItemQueue;
    private final int capacity;

    public Bag(int capacity) {
      bagItemQueue = new ArrayBlockingQueue<>(capacity);
      this.capacity = capacity;
    }

    public boolean offer(T t) {
      return bagItemQueue.offer(t);
    }

    public boolean isFull() {
      return bagItemQueue.size() == capacity;
    }

    public List<T> getAll() {
      if (bagItemQueue.isEmpty()) {
        return Collections.emptyList();
      }

      List<T> list = new ArrayList<>();
      while (!bagItemQueue.isEmpty()) {
        list.add(bagItemQueue.poll());
      }

      return list;
    }

    public int getSize() {
      return bagItemQueue.size();
    }
  }
}
