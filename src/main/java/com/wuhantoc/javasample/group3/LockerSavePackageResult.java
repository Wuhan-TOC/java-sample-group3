package com.wuhantoc.javasample.group3;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LockerSavePackageResult {

    private String ticket;
    private String errorMessage;
    private boolean success;

    public static LockerSavePackageResult savePackageSuccess(String ticket) {
        return builder().success(true).ticket(ticket).build();
    }

    public static LockerSavePackageResult savePackageFail(String reason) {
        return builder().success(false).errorMessage(reason).build();
    }

}
