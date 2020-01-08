package com.wuhantoc.javasample.group3;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LockerGetPackageResult {
    private String errorMessage;
    private boolean successFlag;

    public static LockerGetPackageResult getPackageSuccess() {
        return builder().successFlag(true).build();
    }

    public static LockerGetPackageResult getPackageFail(String reason) {
        return builder().successFlag(false).errorMessage(reason).build();
    }
}
