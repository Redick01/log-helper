package com.redick.example.motan.controller;

import com.redick.annotation.LogMarker;
import com.redick.example.motan.api.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 */
@RestController
public class TestController {

    @Autowired
    private FooService fooService;

    @LogMarker(businessDescription = "/foo/get")
    @GetMapping("/foo/get")
    public String test() {
        return fooService.hello("hhh");
    }
}
