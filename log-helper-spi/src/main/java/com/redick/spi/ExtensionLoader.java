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

package com.redick.spi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liupenghui
 *  2021/12/24 3:22 下午
 */
@Slf4j
@SuppressWarnings("all")
public class ExtensionLoader<T> {

    /**
     * SPI配置扩展的文件位置
     * 扩展文件命名格式为 SPI接口的全路径名，如：com.redick.spi.test.TestSPI
     */
    private static final String DEFAULT_DIRECTORY = "META-INF/log-helper/";

    /**
     * 扩展接口 {@link Class}
     */
    private final Class<T> tClass;

    /**
     * 扩展接口 和 扩展加载器 {@link ExtensionLoader} 的缓存
     */
    private static final Map<Class<?>, ExtensionLoader<?>> MAP = new ConcurrentHashMap<>();

    /**
     * 保存 "扩展" 实现的 {@link Class}
     */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    /**
     * "扩展名" 对应的 保存扩展对象的Holder的缓存
     */
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 扩展class 和 扩展点的实现对象的缓存
     */
    private final Map<Class<?>, Object> joinInstances = new ConcurrentHashMap<>();

    /**
     * 扩展点默认的 "名称" 缓存
     */
    private String cacheDefaultName;

    public ExtensionLoader(final Class<T> tClass) {
        this.tClass = tClass;
        if (tClass != ExtensionFactory.class) {
            ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getExtensionClasses();
        }
    }
    
    public T getDefaultJoin() {
        getExtensionClasses();
        if (StringUtils.isNotBlank(cacheDefaultName)) {
            return getJoin(cacheDefaultName);
        }
        return null;
    }

    public T getJoin(String cacheDefaultName) {
        // 扩展名 文件中的key
        if (StringUtils.isBlank(cacheDefaultName)) {
            throw new IllegalArgumentException("join name is null");
        }
        // 扩展对象存储缓存
        Holder<Object> objectHolder = cachedInstances.get(cacheDefaultName);
        // 如果扩展对象的存储是空的，创建一个扩展对象存储并缓存
        if (null == objectHolder) {
            cachedInstances.putIfAbsent(cacheDefaultName, new Holder<>());
            objectHolder = cachedInstances.get(cacheDefaultName);
        }
        // 从扩展对象的存储中获取扩展对象
        Object value = objectHolder.getT();
        // 如果对象是空的，就触发创建扩展，否则直接返回扩展对象
        if (null == value) {
            synchronized (cacheDefaultName) {
                value = objectHolder.getT();
                if (null == value) {
                    // 创建扩展对象
                    value = createExtension(cacheDefaultName);
                    objectHolder.setT(value);
                }
            }
        }
        return (T) value;
    }

    private Object createExtension(String cacheDefaultName) {
        // 根据扩展名字获取扩展的Class，从Holder中获取 key-value缓存，然后根据名字从Map中获取扩展实现Class
        Class<?> aClass = getExtensionClasses().get(cacheDefaultName);
        if (null == aClass) {
            throw new IllegalArgumentException("extension class is null");
        }
        Object o = joinInstances.get(aClass);
        if (null == o) {
            try {
                // 创建扩展对象并放到缓存中
                joinInstances.putIfAbsent(aClass, aClass.newInstance());
                o = joinInstances.get(aClass);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    public static<T> ExtensionLoader<T> getExtensionLoader(final Class<T> tClass) {
        // 参数非空校验
        if (null == tClass) {
            throw new NullPointerException("tClass is null !");
        }
        // 参数应该是接口
        if (!tClass.isInterface()) {
            throw new IllegalArgumentException("tClass :" + tClass + " is not interface !");
        }
        // 参数要包含@SPI注解
        if (!tClass.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("tClass " + tClass + "without @" + SPI.class + " Annotation !");
        }
        // 从缓存中获取扩展加载器，如果存在直接返回，如果不存在就创建一个扩展加载器并放到缓存中
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) MAP.get(tClass);
        if (null != extensionLoader) {
            return extensionLoader;
        }
        MAP.putIfAbsent(tClass, new ExtensionLoader<>(tClass));
        return (ExtensionLoader<T>) MAP.get(tClass);
    }

    public Map<String, Class<?>> getExtensionClasses() {
        // 扩区SPI扩展实现的缓存，对应的就是扩展文件中的 key - value
        Map<String, Class<?>> classes = cachedClasses.getT();
        if (null == classes) {
            synchronized (cachedClasses) {
                classes = cachedClasses.getT();
                if (null == classes) {
                    // 加载扩展
                    classes = loadExtensionClass();
                    // 缓存扩展实现集合
                    cachedClasses.setT(classes);
                }
            }
        }
        return classes;
    }

    public Map<String, Class<?>> loadExtensionClass() {
        // 扩展接口tClass，必须包含SPI注解
        SPI annotation = tClass.getAnnotation(SPI.class);
        if (null != annotation) {
            String v = annotation.value();
            if (StringUtils.isNotBlank(v)) {
                // 如果有默认的扩展实现名，用默认的
                cacheDefaultName = v;
            }
        }
        Map<String, Class<?>> classes = new HashMap<>(16);
        // 从文件加载
        loadDirectory(classes);
        return classes;
    }

    private void loadDirectory(final Map<String, Class<?>> classes) {
        // 文件名
        String fileName = DEFAULT_DIRECTORY + tClass.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            // 读取配置文件
            Enumeration<URL> urls = classLoader != null ? classLoader.getResources(fileName)
                    : ClassLoader.getSystemResources(fileName);
            if (urls != null) {
                // 获取所有的配置文件
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    // 加载资源
                    loadResources(classes, url);
                }
            }
        } catch (IOException e) {
            log.error("load directory error {}", fileName, e);
        }
    }

    private void loadResources(Map<String, Class<?>> classes, URL url) {
        // 读取文件到Properties，遍历Properties，得到配置文件key（名字）和value（扩展实现的Class）
        try (InputStream inputStream = url.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                // 扩展实现的名字
                String name = (String) k;
                // 扩展实现的Class的全路径
                String classPath = (String) v;
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                    try {
                        // 加载扩展实现Class，就是想其缓存起来，缓存到集合中
                        loadClass(classes, name, classPath);
                    } catch (ClassNotFoundException e) {
                        log.error("load class not found", e);
                    }
                }
            });
        } catch (IOException e) {
            log.error("load resouces error", e);
        }
    }

    private void loadClass(Map<String, Class<?>> classes, String name, String classPath) throws ClassNotFoundException {
        // 反射创建扩展实现的Class
        Class<?> subClass = Class.forName(classPath);
        // 扩展实现的Class要是扩展接口的实现类
        if (!tClass.isAssignableFrom(subClass)) {
            throw new IllegalArgumentException("load extension class error " + subClass + " not sub type of " + tClass);
        }
        // 扩展实现要有Join注解
        Join annotation = subClass.getAnnotation(Join.class);
        if (null == annotation) {
            throw new IllegalArgumentException("load extension class error " + subClass + " without @Join" +
                    "Annotation");
        }
        // 缓存扩展实现Class
        Class<?> oldClass = classes.get(name);
        if (oldClass == null) {
            classes.put(name, subClass);
        } else if (oldClass != subClass) {
            log.error("load extension class error, Duplicate class oldClass is " + oldClass + "subClass is" + subClass);
        }
    }

    public static class Holder<T> {

        private volatile T t;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }
}
