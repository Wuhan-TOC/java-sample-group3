package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserSuperRobotAccessLocker;

import java.util.Random;
import java.util.function.Supplier;

public class UserSuperRobotLockerWithCustomDigitTicket extends AbstractUserSuperRobotLocker implements UserSuperRobotAccessLocker {

    private final int digitCount;
    private final int ticketBound;
    private final Random random = new Random();

    private UserSuperRobotLockerWithCustomDigitTicket(int capacity, int digitCount, Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        super(capacity, lockerBoxSupplier);
        this.digitCount = digitCount;
        this.ticketBound = (int) Math.pow(10, digitCount);
    }

    public static UserSuperRobotAccessLocker initLocker(int capacity, int digitCount, Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        return new UserSuperRobotLockerWithCustomDigitTicket(capacity, digitCount, lockerBoxSupplier);
    }

    @Override
    String generateTicket() {
        return String.format("%" + digitCount + "d", random.nextInt(ticketBound));
    }

}
