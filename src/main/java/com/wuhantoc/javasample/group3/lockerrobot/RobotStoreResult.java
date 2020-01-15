package com.wuhantoc.javasample.group3.lockerrobot;

import com.wuhantoc.javasample.group3.locker.Locker;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotStoreResult {

    private final boolean success;

    private final String robotTicket;

    private final String lockerTicket;

    private final Locker locker;

    private final String errorMessage;

    public static RobotStoreResult storeRobotSuccess(String robotTicket, String lockerTicket, Locker locker) {
        return builder().success(true).robotTicket(robotTicket).lockerTicket(lockerTicket).locker(locker).build();
    }

    public static RobotStoreResult storeRobotFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
