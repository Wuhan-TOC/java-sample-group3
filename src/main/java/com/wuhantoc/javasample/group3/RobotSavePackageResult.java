package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotSavePackageResult {

    private final Locker locker;
    private String ticket;
    private String errorMessage;
    private boolean success;

    public static RobotSavePackageResult savePackageSuccess(LockerSavePackageResult lockerResult, Locker locker) {
        return builder().success(true).ticket(lockerResult.getTicket()).locker(locker).build();
    }

    public static RobotSavePackageResult savePackageFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
