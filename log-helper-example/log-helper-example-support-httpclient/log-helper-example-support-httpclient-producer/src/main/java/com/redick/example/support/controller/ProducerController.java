package com.redick.example.support.controller;

import com.redick.annotation.LogMarker;
import com.redick.example.dto.RequestDTO;
import com.redick.example.dto.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 *  2022/3/31 13:27
 */
@RestController
public class ProducerController {

    @PostMapping("/say")
    @LogMarker(businessDescription = "say", interfaceName = "com.redick.example.support.controller.ProducerController#say()")
    public @ResponseBody
    ResponseDTO say(@RequestBody RequestDTO requestDTO) {
        return new ResponseDTO(0, "success", requestDTO.getContent());
    }
}
