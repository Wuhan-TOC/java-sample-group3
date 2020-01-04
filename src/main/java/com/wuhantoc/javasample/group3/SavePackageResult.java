package com.wuhantoc.javasample.group3;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavePackageResult {

    private String ticket;
    private String errorMessage;
    private boolean successFlag;

    public static SavePackageResult savePackageSuccess(String ticket) {
        return builder().successFlag(true).ticket(ticket).build();
    }

    public static SavePackageResult savePackageFail(String reason) {
        return builder().successFlag(false).errorMessage(reason).build();
    }

}
