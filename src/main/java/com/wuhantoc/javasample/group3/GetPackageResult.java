package com.wuhantoc.javasample.group3;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPackageResult {
    private String errorMessage;
    private boolean success;

    public static GetPackageResult getPackageSuccess() {
        return builder().success(true).build();
    }

    public static GetPackageResult getPackageFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }
}
