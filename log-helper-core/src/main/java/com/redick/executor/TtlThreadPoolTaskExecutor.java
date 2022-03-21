package com.redick.executor;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author liupenghui
 * @date 2021/8/8 7:23 下午
 */
public class TtlThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(@NonNull Runnable command) {
        Runnable ttlRunnable = TtlRunnable.get(command);
        assert ttlRunnable != null;
        super.execute(ttlRunnable);
    }

    @Override
    public @NonNull <T> Future<T> submit(@NonNull Callable<T> task) {
        Callable<T> ttCallable = TtlCallable.get(task);
        return super.submit(ttCallable);
    }

    @Override
    public @NonNull Future<?> submit(@NonNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        assert ttlRunnable != null;
        return super.submit(ttlRunnable);
    }

    @Override
    public @NonNull ListenableFuture<?> submitListenable(@NonNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        assert ttlRunnable != null;
        return super.submitListenable(ttlRunnable);
    }

    @Override
    public @NonNull <T> ListenableFuture<T> submitListenable(@NonNull Callable<T> task) {
        Callable<T> ttlCallable = TtlCallable.get(task);
        return super.submitListenable(ttlCallable);
    }
}
