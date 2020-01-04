package com.wuhantoc.javasample.group3;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPackageResult {
    private String errorMessage;
    private boolean successFlag;

    public static GetPackageResult getPackageSuccess() {
        return builder().successFlag(true).build();
    }

    public static GetPackageResult getPackageFail(String reason) {
        return builder().successFlag(false).errorMessage(reason).build();
    }
}
