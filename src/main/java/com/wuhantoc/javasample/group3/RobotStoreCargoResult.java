package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotStoreCargoResult {

    private final boolean success;

    private final String robotTicket;

    private final String lockerTicket;

    private final UserAccessLocker locker;

    private final String errorMessage;

    public static RobotStoreCargoResult storeCargoSuccess(String robotTicket, String lockerTicket, UserAccessLocker locker) {
        return builder().success(true).robotTicket(robotTicket).lockerTicket(lockerTicket).locker(locker).build();
    }

    public static RobotStoreCargoResult storeCargoFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
