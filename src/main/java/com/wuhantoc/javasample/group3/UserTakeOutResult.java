package com.wuhantoc.javasample.group3;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UserTakeOutResult {

    private final boolean success;

    private final UserAccessLockerBox lockerBox;

    private final String errorMessage;

    public static UserTakeOutResult takeOutSuccess(UserAccessLockerBox lockerBox) {
        return builder().success(true).lockerBox(Objects.requireNonNull(lockerBox)).build();
    }

    public static UserTakeOutResult takeOutFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
