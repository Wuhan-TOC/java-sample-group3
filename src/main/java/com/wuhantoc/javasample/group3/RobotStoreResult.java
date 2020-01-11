package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotStoreResult {

    private final boolean success;

    private final String ticket;

    private final RobotAccessLockerBox lockerBox;

    private final String errorMessage;

    public static RobotStoreResult storeSuccess(String ticket, RobotAccessLockerBox lockerBox) {
        return builder().success(true).ticket(ticket).lockerBox(lockerBox).build();
    }

    public static RobotStoreResult storeFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
