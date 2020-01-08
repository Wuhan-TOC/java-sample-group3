package com.wuhantoc.javasample.group3;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageFail;
import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageSuccess;
import static com.wuhantoc.javasample.group3.LockerSavePackageResult.savePackageFail;
import static com.wuhantoc.javasample.group3.LockerSavePackageResult.savePackageSuccess;

@Data
public class Locker {

    static final String FULL_LOCKER_MESSAGE = "Locker is full";

    static final String WRONG_TICKET_MESSAGE = "Your ticket is not correct";

    private final int capacity;

    private final Set<String> storage;

    public Locker(int capacity) {
        this.capacity = capacity;
        storage = new HashSet<>(capacity);
    }

    public LockerSavePackageResult savePackage() {
        if (capacity == storage.size()) {
            return savePackageFail(FULL_LOCKER_MESSAGE);
        }
        String ticket = UUID.randomUUID().toString();
        while (!storage.add(ticket)) {
            ticket = UUID.randomUUID().toString();
        }
        return savePackageSuccess(ticket);
    }

    public GetPackageResult getPackage(String ticket) {
        if (storage.remove(ticket)) {
            return getPackageSuccess();
        } else {
            return getPackageFail(WRONG_TICKET_MESSAGE);
        }
    }


}
