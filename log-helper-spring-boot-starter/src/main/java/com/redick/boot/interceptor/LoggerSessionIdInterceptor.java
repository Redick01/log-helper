package com.redick.boot.interceptor;

import com.redick.common.GlobalSessionIdDefine;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author liupenghui
 * @date 2021/7/7 11:36 下午
 */
@Aspect
@Configuration
@SuppressWarnings("all")
public class LoggerSessionIdInterceptor {

    /**
     * requestMapping
     * putMapping
     * postMapping
     * patchMapping
     * modelAttribute
     * getMapping
     * deleteMapping
     * CrossOrigin
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)"+
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)"+
            "||@annotation(org.springframework.web.bind.annotation.ModelAttribute)"+
            "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "||@annotation(org.springframework.web.bind.annotation.CrossOrigin)")
    public void match() {

    }

    /**
     * 执行结果
     * @param joinPoint 切点
     * @return 返回结果
     */
    @Around("match()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String sessionId = MDC.get(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY);
        if(StringUtils.isEmpty(sessionId)){
            sessionId = UUID.randomUUID().toString();
            MDC.put(GlobalSessionIdDefine.GLOBAL_SESSION_ID_KEY,sessionId);
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
