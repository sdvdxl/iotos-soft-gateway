package me.hekr.iotos.softgateway.common.utils;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import java.io.Closeable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/** @author du */
@Slf4j
public class ThreadPoolUtil implements Closeable {
  public static final UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER =
      (t, e) ->
          log.error(
              "[thread:" + t.getName() + "] priority:" + t.getPriority() + ", " + e.getMessage(),
              e);

  private static final int CORES = Runtime.getRuntime().availableProcessors();
  private static final ConcurrentHashMap<String, ThreadPoolExecutor>
      THREAD_POOL_EXECUTOR_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

  public static final ScheduledExecutorService DEFAULT_SCHEDULED =
      (ScheduledExecutorService)
          new Builder()
              .setPrefix("default-scheduled")
              .setCore(CORES + 1)
              .setMax(16)
              .setQueueSize(1000)
              .setScheduled(true)
              .build();

  static {
    final StringBuilder message = new StringBuilder();
    // 设置每分钟打印一次线程池状态
    ScheduledThreadPoolExecutor timerExecutor =
        (ScheduledThreadPoolExecutor)
            new Builder()
                .setCore(1)
                .setPrefix("thread-pool-util-monitor")
                .setScheduled(true)
                .build();
    timerExecutor.scheduleAtFixedRate(
        () -> {
          message.delete(0, message.length());
          THREAD_POOL_EXECUTOR_CONCURRENT_HASH_MAP.forEach(
              (k, v) -> message.append(k).append(", ").append(v.toString()).append("\n"));
          log.info(
              "\nall thread pools(count: {}) status:\n{}",
              THREAD_POOL_EXECUTOR_CONCURRENT_HASH_MAP.size(),
              message.toString());
        },
        0,
        1,
        TimeUnit.MINUTES);
  }

  static {
    log.info("enable all thread exception log");
    Thread.setDefaultUncaughtExceptionHandler(DEFAULT_UNCAUGHT_EXCEPTION_HANDLER);
  }

  public static ThreadPoolExecutor get(String name) {
    return THREAD_POOL_EXECUTOR_CONCURRENT_HASH_MAP.get(name);
  }

  /**
   * 是否开启记录所有的线程异常信息，建议开启，默认开启
   *
   * @param enable true 开启，false 关闭
   */
  public static void logAllThreadException(boolean enable) {
    Thread.setDefaultUncaughtExceptionHandler(enable ? DEFAULT_UNCAUGHT_EXCEPTION_HANDLER : null);
  }

  @Override
  public void close() {
    THREAD_POOL_EXECUTOR_CONCURRENT_HASH_MAP.values().forEach(ThreadPoolExecutor::shutdown);
  }

  @Getter
  public static class Builder {

    /** 当大于等于该值的时候根据cpu核数动态设置core和max */
    private int dynamicCount;

    /** 核心数量,默认1 */
    private int core = 1;

    /** 最大数量，默认cpu最大数量2倍 */
    private int max = core * 2;

    private int queueSize = -1;

    /** 存活时间，默认0,永久 */
    private long keepAliveTime = 0;

    /**
     * 存活时间 默认毫秒数，永久
     *
     * @see #keepAliveTime
     */
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    /** 拒绝策略，默认 AbortPolicy */
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    /** 线城池名字前缀，并且唯一标识线城池 */
    private String format;

    private UncaughtExceptionHandler uncaughtExceptionHandler = DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
    private boolean scheduled;

    public Builder setCore(int core) {
      this.core = core;
      return this;
    }

    public Builder setMax(int max) {
      this.max = max;
      return this;
    }

    public Builder setQueueSize(int queueSize) {
      this.queueSize = queueSize;
      return this;
    }

    public Builder setKeepAliveTime(long keepAliveTime) {
      this.keepAliveTime = keepAliveTime;
      return this;
    }

    public Builder setTimeUnit(TimeUnit timeUnit) {
      this.timeUnit = timeUnit;
      return this;
    }

    public Builder setHandler(RejectedExecutionHandler handler) {
      this.handler = handler;
      return this;
    }

    public Builder setPrefix(String prefix) {
      this.format = prefix + "-";
      return this;
    }

    /**
     * 设置core和max根据cpu核数根据cpu核数设置
     *
     * @param dynamic 当大于等于该值的时候生效，如果小于等于0不生效
     * @return builder
     */
    public Builder setCoresDynamic(int dynamic) {
      this.dynamicCount = dynamic;
      return this;
    }

    /**
     * 设置异常处理
     *
     * @param uncaughtExceptionHandler 处理器
     * @return builder
     */
    public Builder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
      this.uncaughtExceptionHandler = uncaughtExceptionHandler;
      return this;
    }

    public Builder setScheduled(boolean scheduled) {
      this.scheduled = scheduled;
      return this;
    }

    /**
     * 如果名字一样，则返回已经存在的
     *
     * @return ThreadPoolExecutor
     */
    public synchronized ThreadPoolExecutor build() {

      String poolName = StringUtils.defaultString(format, "default-pool-thread-%s");
      return THREAD_POOL_EXECUTOR_CONCURRENT_HASH_MAP.computeIfAbsent(
          poolName,
          name -> {
            ThreadFactoryBuilder threadFactoryBuilder =
                new ThreadFactoryBuilder().setNamePrefix(poolName);
            if (uncaughtExceptionHandler != null) {
              threadFactoryBuilder.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            }

            if (CORES >= this.dynamicCount && this.dynamicCount > 0) {
              core = CORES;
              max = core * 2;
              log.info("thread pool {} use dynamic config, core: {}, max: {}", poolName, core, max);
            }

            if (core < 0) {
              log.warn("thread pool {} core must be gte 0, will set to cores: {}", poolName, CORES);
              core = CORES;
            }

            if (max < core) {
              log.warn(
                  "thread pool {} max must be gte core, core: {}, max: {}, will set max eq core",
                  poolName,
                  core,
                  max);
            }

            if (scheduled) {
              return new ScheduledThreadPoolExecutor(core, threadFactoryBuilder.build(), handler);
            }

            return new ThreadPoolExecutor(
                core,
                max,
                keepAliveTime,
                timeUnit,
                new LinkedBlockingQueue<>(queueSize == -1 ? max * 2 : queueSize),
                threadFactoryBuilder.build(),
                handler);
          });
    }
  }

  /**
   * 单进程 executor
   *
   * <p>如果新提交，有正在运行的任务，取消掉； 如果队列满，则丢弃最旧的。
   *
   * <p>注意： 可能当前任务结束不掉，直到运行结束完成，再运行最后提交的任务
   *
   * <p>如果有循环，则循环结束条件因该使用 Thread.interrupted(); 判断是否被打断，返回 true 应该结束循环
   */
  public static class SingleThreadPoolExecutor {
    private ThreadPoolExecutor executor;
    private String name;
    private Object lock = new Object();
    private Future future;

    public SingleThreadPoolExecutor(String name) {
      this.name = name;
      executor =
          new Builder()
              .setPrefix(name)
              .setCore(1)
              .setMax(1)
              .setQueueSize(1)
              .setHandler(new DiscardOldestPolicy())
              .build();
    }

    public synchronized void submit(Runnable runnable) {
      if (future != null) {
        future.cancel(true);
      }

      future = executor.submit(runnable);
    }

    public synchronized Future getFuture() {
      return future;
    }
  }
}
