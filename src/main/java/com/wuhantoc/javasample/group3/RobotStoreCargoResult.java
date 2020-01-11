package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotStoreCargoResult {

    private final boolean success;

    private final String ticket;

    private final UserAccessLocker locker;

    private final String errorMessage;

    public static RobotStoreCargoResult storeCargoSuccess(String ticket, UserAccessLocker locker) {
        return builder().success(true).ticket(ticket).locker(locker).build();
    }

    public static RobotStoreCargoResult storeCargoFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
