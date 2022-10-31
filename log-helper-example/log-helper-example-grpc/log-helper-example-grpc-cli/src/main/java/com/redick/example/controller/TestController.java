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
