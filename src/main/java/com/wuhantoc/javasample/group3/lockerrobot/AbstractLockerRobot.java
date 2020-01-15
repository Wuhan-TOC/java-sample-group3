package com.wuhantoc.javasample.group3.lockerrobot;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.locker.Locker;
import com.wuhantoc.javasample.group3.locker.LockerStoreResult;
import com.wuhantoc.javasample.group3.locker.LockerTakeOutResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.wuhantoc.javasample.group3.lockerrobot.RobotStoreResult.storeRobotFail;
import static com.wuhantoc.javasample.group3.lockerrobot.RobotStoreResult.storeRobotSuccess;
import static com.wuhantoc.javasample.group3.lockerrobot.RobotTakeOutResult.takeOutRobotFail;
import static com.wuhantoc.javasample.group3.lockerrobot.RobotTakeOutResult.takeOutRobotSuccess;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;

abstract class AbstractLockerRobot {

    private final Collection<Locker> lockers;

    private final Map<String, LockerAndTicket> ticketLockerMap;

    protected AbstractLockerRobot(Collection<Locker> lockers) {
        this.lockers = new ArrayList<>(lockers.size());
        this.lockers.addAll(lockers);
        ticketLockerMap = new HashMap<>();
    }

    protected RobotStoreResult storeCargo(Cargo cargo) {
        for (Locker locker : sortLockersBeforeStore(lockers)) {
            LockerStoreResult storeResult = locker.store();
            if (storeResult.isSuccess()) {
                String robotTicket = UUID.randomUUID().toString();
                ticketLockerMap.put(robotTicket, new LockerAndTicket(locker, storeResult.getTicket()));
                storeResult.getLockerBox().store(cargo);
                return storeRobotSuccess(robotTicket, storeResult.getTicket(), locker);
            }
        }
        return storeRobotFail(ROBOT_STORE_FAIL_MESSAGE);
    }

    protected RobotTakeOutResult takeOutCargo(String robotTicket) {
        if (ticketLockerMap.containsKey(robotTicket)) {
            LockerAndTicket lockerAndTicket = ticketLockerMap.remove(robotTicket);
            LockerTakeOutResult takeOutResult = lockerAndTicket.locker.takeOut(lockerAndTicket.ticket);
            if (takeOutResult.isSuccess()) {
                return takeOutRobotSuccess(takeOutResult.getLockerBox().get());
            }
        }
        return takeOutRobotFail(ROBOT_TAKE_OUT_FAIL_MESSAGE);
    }

    protected abstract Collection<Locker> sortLockersBeforeStore(Collection<Locker> lockers);

    private static class LockerAndTicket {
        private Locker locker;
        private String ticket;

        private LockerAndTicket(Locker locker, String ticket) {
            this.locker = locker;
            this.ticket = ticket;
        }
    }

}
