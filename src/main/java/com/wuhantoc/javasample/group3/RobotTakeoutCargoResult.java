package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotTakeoutCargoResult {

    private final boolean success;

    private final Cargo cargo;

    private final String errorMessage;

    public static RobotTakeoutCargoResult takeOutSuccess(Cargo cargo) {
        return builder().success(true).cargo(Objects.requireNonNull(cargo)).build();
    }

    public static RobotTakeoutCargoResult takeOutFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
