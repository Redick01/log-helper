/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 *  2021/8/8 7:23 下午
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
