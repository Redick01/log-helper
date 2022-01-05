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
 * @date 2021/12/24 3:22 下午
 */
@Slf4j
@SuppressWarnings("all")
public class ExtensionLoader<T> {

    private static final String DEFAULT_DIRECTORY = "META-INF/log-helper/";

    private final Class<T> tClass;

    private static final Map<Class<?>, ExtensionLoader<?>> MAP = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Map<Class<?>, Object> joinInstances = new ConcurrentHashMap<>();


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
        if (StringUtils.isBlank(cacheDefaultName)) {
            throw new IllegalArgumentException("join name is null");
        }
        Holder<Object> objectHolder = cachedInstances.get(cacheDefaultName);
        if (null == objectHolder) {
            cachedInstances.putIfAbsent(cacheDefaultName, new Holder<>());
            objectHolder = cachedInstances.get(cacheDefaultName);
        }
        Object value = objectHolder.getT();
        if (null == value) {
            synchronized (cacheDefaultName) {
                value = objectHolder.getT();
                if (null == value) {
                    value = createExtension(cacheDefaultName);
                    objectHolder.setT(value);
                }
            }
        }
        return (T) value;
    }

    private Object createExtension(String cacheDefaultName) {
        Class<?> aClass = getExtensionClasses().get(cacheDefaultName);
        if (null == aClass) {
            throw new IllegalArgumentException("extension class is null");
        }
        Object o = joinInstances.get(aClass);
        if (null == o) {
            try {
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
        if (null == tClass) {
            throw new NullPointerException("tClass is null !");
        }
        if (!tClass.isInterface()) {
            throw new IllegalArgumentException("tClass :" + tClass + " is not interface !");
        }
        if (!tClass.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("tClass " + tClass + "without @" + SPI.class + " Annotation !");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) MAP.get(tClass);
        if (null != extensionLoader) {
            return extensionLoader;
        }
        MAP.putIfAbsent(tClass, new ExtensionLoader<>(tClass));
        return (ExtensionLoader<T>) MAP.get(tClass);
    }

    public Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.getT();
        if (null == classes) {
            synchronized (cachedClasses) {
                classes = cachedClasses.getT();
                if (null == classes) {
                    classes = loadExtensionClass();
                    cachedClasses.setT(classes);
                }
            }
        }
        return classes;
    }

    public Map<String, Class<?>> loadExtensionClass() {
        SPI annotation = tClass.getAnnotation(SPI.class);
        if (null != annotation) {
            String v = annotation.value();
            if (StringUtils.isNotBlank(v)) {
                cacheDefaultName = v;
            }
        }
        Map<String, Class<?>> classes = new HashMap<>(16);
        // 从文件加载
        loadDirectory(classes);
        return classes;
    }

    private void loadDirectory(final Map<String, Class<?>> classes) {
        String fileName = DEFAULT_DIRECTORY + tClass.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader != null ? classLoader.getResources(fileName)
                    : ClassLoader.getSystemResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResources(classes, url);
                }
            }
        } catch (IOException e) {
            log.error("load directory error {}", fileName, e);
        }
    }

    private void loadResources(Map<String, Class<?>> classes, URL url) {
        try (InputStream inputStream = url.openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                String name = (String) k;
                String classPath = (String) v;
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                    try {
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
        Class<?> subClass = Class.forName(classPath);
        if (!tClass.isAssignableFrom(subClass)) {
            throw new IllegalArgumentException("load extension class error " + subClass + " not sub type of " + tClass);
        }
        Join annotation = subClass.getAnnotation(Join.class);
        if (null == annotation) {
            throw new IllegalArgumentException("load extension class error " + subClass + " without @Join" +
                    "Annotation");
        }
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
