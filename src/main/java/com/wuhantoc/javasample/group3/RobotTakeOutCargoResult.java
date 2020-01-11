package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotTakeOutCargoResult {

    private final boolean success;

    private final Cargo cargo;

    private final String errorMessage;

    public static RobotTakeOutCargoResult takeOutSuccess(Cargo cargo) {
        return builder().success(true).cargo(Objects.requireNonNull(cargo)).build();
    }

    public static RobotTakeOutCargoResult takeOutFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
