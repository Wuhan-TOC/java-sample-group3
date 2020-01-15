package com.wuhantoc.javasample.group3.lockerrobot;

import com.wuhantoc.javasample.group3.Cargo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RobotTakeOutResult {

    private final boolean success;

    private final Cargo cargo;

    private final String errorMessage;

    public static RobotTakeOutResult takeOutRobotSuccess(Cargo cargo) {
        return builder().success(true).cargo(Objects.requireNonNull(cargo)).build();
    }

    public static RobotTakeOutResult takeOutRobotFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
