package com.wuhantoc.javasample.group3.locker;

import com.wuhantoc.javasample.group3.lockerbox.LockerBox;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LockerTakeOutResult {

    private final boolean success;

    private final LockerBox lockerBox;

    private final String errorMessage;

    public static LockerTakeOutResult takeOutLockerSuccess(LockerBox lockerBox) {
        return builder().success(true).lockerBox(Objects.requireNonNull(lockerBox)).build();
    }

    public static LockerTakeOutResult takeOutLockerFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
