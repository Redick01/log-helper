package com.redick.starter.importer;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Redick01
 *  2022/3/25 14:29
 */
public class LogHelperImportSelector implements ImportSelector {

    @Override
    @SuppressWarnings("all")
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return Lists.newArrayList("com.redick.starter.configure.LogHelperAutoConfiguration",
                "com.redick.starter.aop.LogHelperAspectJ")
                .toArray(new String[0]);
    }
}
