package com.redick.spi;

/**
 * @author liupenghui
 * @date 2021/12/24 3:13 下午
 */
@SPI("spi")
public interface ExtensionFactory {

    /**
     * 获取扩展
     * @param key key
     * @param clazz Class
     * @param <T> type
     * @return extension
     */
    <T> T getExtension(String key, Class<T> clazz);
}
