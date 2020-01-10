package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UserStoreResult {

    private final boolean success;

    private final String ticket;

    private final UserAccessLockerBox lockerBox;

    private final String errorMessage;

    public static UserStoreResult storeSuccess(String ticket, UserAccessLockerBox lockerBox) {
        return builder().success(true).ticket(ticket).lockerBox(lockerBox).build();
    }

    public static UserStoreResult storeFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
