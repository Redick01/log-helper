package com.redick.example.support.controller;

import com.redick.annotation.LogMarker;
import com.redick.example.dto.Request;
import com.redick.example.dto.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 * @date 2022/3/31 13:27
 */
@RestController
public class ProducerController {

    @PostMapping("/say")
    @LogMarker(businessDescription = "say", interfaceName = "com.redick.example.support.controller.ProducerController#say()")
    public @ResponseBody Response say(@RequestBody Request request) {
        return new Response(0, "success", request.getContent());
    }
}
