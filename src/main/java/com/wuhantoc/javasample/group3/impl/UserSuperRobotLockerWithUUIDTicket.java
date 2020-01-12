package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserSuperRobotAccessLocker;

import java.util.UUID;
import java.util.function.Supplier;

public class UserSuperRobotLockerWithUUIDTicket extends AbstractUserSuperRobotLocker implements UserSuperRobotAccessLocker {

    private UserSuperRobotLockerWithUUIDTicket(int capacity, Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        super(capacity, lockerBoxSupplier);
    }

    public static UserSuperRobotAccessLocker initLocker(int capacity, Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        return new UserSuperRobotLockerWithUUIDTicket(capacity, lockerBoxSupplier);
    }

    @Override
    String generateTicket() {
        return UUID.randomUUID().toString();
    }

}
