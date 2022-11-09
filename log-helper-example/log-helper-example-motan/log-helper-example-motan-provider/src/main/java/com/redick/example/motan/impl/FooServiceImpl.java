package com.redick.example.motan.impl;

import com.redick.annotation.LogMarker;
import com.redick.example.motan.api.FooService;
import org.springframework.stereotype.Component;

/**
 * @author Redick01
 */
@Component
public class FooServiceImpl implements FooService {

    @LogMarker(businessDescription = "hello")
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
