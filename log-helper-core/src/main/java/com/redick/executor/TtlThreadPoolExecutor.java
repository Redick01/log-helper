package com.redick.executor;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author liupenghui
 * @date 2021/8/9 1:31 下午
 */
public class TtlThreadPoolExecutor extends ThreadPoolExecutor {

    public TtlThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TtlThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public TtlThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public TtlThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        Runnable runnable = TtlRunnable.get(command);
        super.execute(runnable);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        Runnable runnable = TtlRunnable.get(task);
        return super.submit(runnable, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        Runnable runnable = TtlRunnable.get(task);
        return super.submit(runnable);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Callable command = TtlCallable.get(task);
        return super.submit(command);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        Runnable command = TtlRunnable.get(runnable);
        return super.newTaskFor(command, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        Callable command = TtlCallable.get(callable);
        return super.newTaskFor(command);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        Collection<? extends Callable<T>> callables = TtlCallable.gets(tasks);
        return super.invokeAny(callables);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Collection<? extends Callable<T>> callables = TtlCallable.gets(tasks);
        return super.invokeAny(callables, timeout, unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        Collection<? extends Callable<T>> callables = TtlCallable.gets(tasks);
        return super.invokeAll(callables);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        Collection<? extends Callable<T>> callables = TtlCallable.gets(tasks);
        return super.invokeAll(callables, timeout, unit);
    }
}
