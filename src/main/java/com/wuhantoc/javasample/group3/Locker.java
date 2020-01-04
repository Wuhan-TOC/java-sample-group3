package com.wuhantoc.javasample.group3;

import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageFail;
import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageSuccess;
import static com.wuhantoc.javasample.group3.SavePackageResult.savePackageFail;
import static com.wuhantoc.javasample.group3.SavePackageResult.savePackageSuccess;

@Data
public class Locker {

    static final String FULL_LOCKER_MESSAGE = "Locker is full";

    static final String WRONG_TICKET_MESSAGE = "Your ticket is not correct";

    private static final int STORAGE_SIZE = 24;

    private final Set<String> storage = new HashSet<>(STORAGE_SIZE);

    void fillToTheFull() {
        while (storage.size() != STORAGE_SIZE) {
            storage.add(Objects.toString(storage.size()));
        }
    }

    String generateCorrectTicket() {
        return storage.stream().findAny().orElse(savePackage().getTicket());
    }

    String generateWrongTicket() {
        String wrongTicket = UUID.randomUUID().toString();
        while (storage.contains(wrongTicket)) {
            wrongTicket = UUID.randomUUID().toString();
        }
        return wrongTicket;
    }

    public SavePackageResult savePackage() {
        if (STORAGE_SIZE == storage.size()) {
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
