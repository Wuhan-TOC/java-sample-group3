package com.wuhantoc.javasample.group3;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageFail;
import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageSuccess;

public class LockerRobot {

    static final String LOCKERS_ARE_ALL_FULL_MESSAGES = "All the lockers are full, save failed";

    static final String WRONG_TICKET_MESSAGE = "Robot failed to find any package with the given ticket in its lockers";

    static final String USED_TICKET_MESSAGE = "Robot failed to find the package with the given ticket, maybe the ticket was used";

    private final Iterable<Locker> lockers;

    private final Map<String, Locker> ticketLockerCache;

    public LockerRobot(Iterable<Locker> lockers) {
        this.lockers = Objects.requireNonNull(lockers);
        ticketLockerCache = new HashMap<>();
    }

    public RobotSavePackageResult savePackage() {
        for (Locker locker : lockers) {
            LockerSavePackageResult savePackageResult = locker.savePackage();
            if (savePackageResult.isSuccess()) {
                ticketLockerCache.put(savePackageResult.getTicket(), locker);
                return RobotSavePackageResult.savePackageSuccess(savePackageResult, locker);
            }
        }
        return RobotSavePackageResult.savePackageFail(LOCKERS_ARE_ALL_FULL_MESSAGES);
    }

    public GetPackageResult getPackage(String ticket) {
        if (ticketLockerCache.containsKey(ticket)) {
            Locker locker = ticketLockerCache.remove(ticket);
            GetPackageResult lockerResult = locker.getPackage(ticket);
            if (lockerResult.isSuccess()) {
                return getPackageSuccess();
            } else {
                return getPackageFail(USED_TICKET_MESSAGE);
            }
        }
        return getPackageFail(WRONG_TICKET_MESSAGE);
    }
}
