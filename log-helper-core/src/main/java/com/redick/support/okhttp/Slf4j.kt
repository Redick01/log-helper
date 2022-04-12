package com.redick.support.okhttp

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Redick01
 *  2022/3/31 18:40
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4j {

    companion object{
        val <reified T> T.log: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}
