package com.redick.boot;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liupenghui
 * @date 2021/7/7 11:34 下午
 */
public class ImportedClassSelector implements ImportSelector {

    @SuppressWarnings("all")
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> imports = new ArrayList<>();
        imports.add("com.log.helper.interceptor.LoggerSessionIdInterceptor");
        imports.add("com.log.helper.interceptor.LoggerAnnotationInterceptor");
        return imports.toArray(new String[0]);
    }
}
