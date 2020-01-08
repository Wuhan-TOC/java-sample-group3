package com.wuhantoc.javasample.group3;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Getter
public class RobotSavePackageResult {

    private final LockerSavePackageResult lockerResult;
    private final Locker locker;

    public RobotSavePackageResult(LockerSavePackageResult lockerResult, Locker locker) {
        this.lockerResult = Objects.requireNonNull(lockerResult);
        this.locker = locker;
    }

}
