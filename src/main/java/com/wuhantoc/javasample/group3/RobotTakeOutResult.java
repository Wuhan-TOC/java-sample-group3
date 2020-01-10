package com.wuhantoc.javasample.group3;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotTakeOutResult {

    private final boolean success;

    private final RobotAccessLockerBox lockerBox;

    private final String errorMessage;

    public static RobotTakeOutResult takeOutSuccess(RobotAccessLockerBox lockerBox) {
        return builder().success(true).lockerBox(Objects.requireNonNull(lockerBox)).build();
    }

    public static RobotTakeOutResult takeOutFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
