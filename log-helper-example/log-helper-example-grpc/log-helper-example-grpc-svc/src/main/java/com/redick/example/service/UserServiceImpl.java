package com.redick.example.service;

import com.redick.annotation.LogMarker;
import com.redick.example.protocol.User;
import com.redick.example.protocol.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author Redick01
 */
@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @LogMarker(businessDescription = "获取用户")
    @Override
    public void getUser(User request, StreamObserver<User> responseObserver) {
        User user = User.newBuilder()
                .setName("response name")
                .build();
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @LogMarker(businessDescription = "获取用户列表")
    @Override
    public void getUsers(User request, StreamObserver<User> responseObserver) {
        User user = User.newBuilder()
                .setName("user1")
                .build();
        User user2 = User.newBuilder()
                .setName("user2")
                .build();
        responseObserver.onNext(user);
        responseObserver.onNext(user2);

        responseObserver.onCompleted();
    }

    @LogMarker(businessDescription = "保存用户")
    @Override
    public StreamObserver<User> saveUsers(StreamObserver<User> responseObserver) {
        return new StreamObserver<User>() {
            @Override
            public void onNext(User user) {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                User user = User.newBuilder()
                        .setName("saveUsers user1")
                        .build();
                responseObserver.onNext(user);
                responseObserver.onCompleted();
            }
        };
    }
}
