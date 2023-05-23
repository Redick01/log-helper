/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.example.controller;

import com.redick.annotation.LogMarker;
import com.redick.example.protocol.User;
import com.redick.example.protocol.UserServiceGrpc;
import com.redick.starter.interceptor.GrpcInterceptor;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Redick01
 */
@RestController
public class TestController {

    @GrpcClient("userClient")
    private UserServiceGrpc.UserServiceBlockingStub userService;

    @Autowired
    private GrpcInterceptor grpcInterceptor;

    @LogMarker(businessDescription = "获取用户名")
    @GetMapping("/getUser")
    public String getUser()     {
        User user = User.newBuilder()
                .setUserId(100)
                .putHobbys("pingpong", "play pingpong")
                .setCode(200)
                .build();
        Channel channel = ClientInterceptors.intercept(userService.getChannel(), grpcInterceptor);
        userService = UserServiceGrpc.newBlockingStub(channel);
        User u = userService.getUser(user);
        return u.getName();
    }
}
