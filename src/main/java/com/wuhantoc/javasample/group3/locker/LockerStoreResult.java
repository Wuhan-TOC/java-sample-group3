package com.wuhantoc.javasample.group3.locker;

import com.wuhantoc.javasample.group3.lockerbox.LockerBox;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LockerStoreResult {

    private final boolean success;

    private final String ticket;

    private final LockerBox lockerBox;

    private final String errorMessage;

    public static LockerStoreResult storeLockerSuccess(String ticket, LockerBox lockerBox) {
        return builder().success(true).ticket(ticket).lockerBox(lockerBox).build();
    }

    public static LockerStoreResult storeLockerFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
