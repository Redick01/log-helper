package com.redick.example.support.service;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Request;
import com.redick.example.dto.RequestDTO;
import com.redick.support.forest.TraceIdForestInterceptor;

/**
 * @author Redick01
 */
public interface ForestClientService {

    @Request(
            url = "http://localhost:8783/producer/say",
            type = "POST",
            interceptor = {TraceIdForestInterceptor.class}
    )
    String say(@JSONBody RequestDTO requestDTO);
}
