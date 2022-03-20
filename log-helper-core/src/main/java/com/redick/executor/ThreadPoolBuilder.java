package com.redick.executor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author liupenghui
 * @date 2022/3/20 10:56
 */
public class ThreadPoolBuilder {

    /**
     * name of threadPool.
     */
    private String threadPoolName;

    /**
     * CoreSize of ThreadPool.
     */
    private int corePoolSize = 1;

    /**
     * MaxSize of ThreadPool.
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * When the number of threads is greater than the core,
     * this is the maximum time that excess idle threads
     * will wait for new tasks before terminating
     */
    private long keepAliveTime = 30;

    /**
     * Timeout unit.
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * Queue capacity
     */
    private int queueCapacity = 1024;

    /**
     * Blocking queue
     */
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueCapacity);

    /**
     * RejectedExecutionHandler
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * Default inner thread factory.
     */
    private ThreadFactory threadFactory = null;
    /**
     * If allow core thread timeout.
     */
    private boolean allowCoreThreadTimeOut = false;

    /**
     * Whether to wait for scheduled tasks to complete on shutdown,
     * not interrupting running tasks and executing all tasks in the queue.
     */
    private boolean waitForTasksToCompleteOnShutdown = false;

    /**
     * The maximum number of seconds that this executor is supposed to block
     * on shutdown in order to wait for remaining tasks to complete their execution
     * before the rest of the container continues to shut down.
     */
    private int awaitTerminationSeconds = 0;

    /**
     * If io intensive thread pool.
     * default false, true indicate cpu intensive thread pool.
     */
    private boolean ioIntensive = false;

    public ThreadPoolBuilder() {
    }

    public static ThreadPoolBuilder builder() {
        return new ThreadPoolBuilder();
    }

    public ThreadPoolBuilder threadPoolName(String poolName) {
        this.threadPoolName = poolName;
        return this;
    }

    public ThreadPoolBuilder corePoolSize(int corePoolSize) {
        if (corePoolSize >= 0) {
            this.corePoolSize = corePoolSize;
        }
        return this;
    }

    public ThreadPoolBuilder maximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize > 0) {
            this.maximumPoolSize = maximumPoolSize;
        }
        return this;
    }

    public ThreadPoolBuilder keepAliveTime(long keepAliveTime) {
        if (keepAliveTime > 0) {
            this.keepAliveTime = keepAliveTime;
        }
        return this;
    }

    public ThreadPoolBuilder timeUnit(TimeUnit timeUnit) {
        if (timeUnit != null) {
            this.timeUnit = timeUnit;
        }
        return this;
    }



    public ThreadPoolBuilder rejectedExecutionHandler(RejectedExecutionHandler handler) {
        if (!Objects.isNull(handler)) {
            rejectedExecutionHandler = handler;
        }
        return this;
    }

    public ThreadPoolBuilder threadFactory(String prefix) {
        if (StringUtils.isNotBlank(prefix)) {
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(StrUtil.format("{}", prefix));
                    t.setDaemon(false);
                    return t;
                }
            };

        }
        return this;
    }

    public ThreadPoolBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        return this;
    }


    public ThreadPoolBuilder awaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
        return this;
    }

    public ThreadPoolBuilder waitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        return this;
    }

    public ThreadPoolBuilder ioIntensive(boolean ioIntensive) {
        this.ioIntensive = ioIntensive;
        return this;
    }

    public ThreadPoolBuilder queueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        return this;
    }

    /**
     * Build according to dynamic field.
     *
     * @return the newly created ThreadPoolExecutor instance
     */
    public ExecutorService build() {
        return TtlExecutors.getTtlExecutorService(buildCommonExecutor());
    }

    /**
     * Build common threadPoolExecutor, does not manage by DynamicTp framework.
     *
     * @return the newly created ThreadPoolExecutor instance
     */
    private ThreadPoolExecutor buildCommonExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                this.corePoolSize,
                this.maximumPoolSize,
                this.keepAliveTime,
                this.timeUnit,
                this.workQueue,
                this.threadFactory,
                this.rejectedExecutionHandler
        );
        executor.allowCoreThreadTimeOut(this.allowCoreThreadTimeOut);
        return executor;
    }
}
