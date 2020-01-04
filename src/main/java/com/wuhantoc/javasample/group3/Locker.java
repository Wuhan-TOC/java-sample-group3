package com.wuhantoc.javasample.group3;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
public class Locker {

    static final String FULL_LOCKER_MESSAGE = "Locker is full";

    static final String WRONG_TICKET_MESSAGE = "Your ticket is not correct";

    private static final int STORAGE_SIZE = 24;

    private boolean FullLocker;

    private final Set<String> storage = new HashSet<>(STORAGE_SIZE);

    void fillToTheFull() {
        while (storage.size() != STORAGE_SIZE) {
            storage.add(Objects.toString(storage.size()));
        }
    }

    public SavePackageResult savePackage() {
        SavePackageResult result = new SavePackageResult();
        if (STORAGE_SIZE == storage.size()) {
            result.setTicket(null);
            result.setSuccesssFlag(false);
            result.setErrorMessage(FULL_LOCKER_MESSAGE);
            return result;
        }
        String ticket = UUID.randomUUID().toString();
        while (!storage.add(ticket)) {
            ticket = UUID.randomUUID().toString();
        }
        result.setTicket(ticket);
        result.setSuccesssFlag(true);
        result.setErrorMessage(null);
        return result;
    }

    public GetPackageResult getPackage(String ticket) {
        GetPackageResult result = new GetPackageResult();
        if (storage.remove(ticket)) {
            result.setSuccessFlag(true);
            result.setErrorMessage(null);
        } else {
            result.setSuccessFlag(false);
            result.setErrorMessage(WRONG_TICKET_MESSAGE);
        }
        return result;
    }


}
