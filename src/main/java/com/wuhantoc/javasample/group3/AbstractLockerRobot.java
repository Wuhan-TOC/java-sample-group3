package com.wuhantoc.javasample.group3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.wuhantoc.javasample.group3.RobotStoreCargoResult.storeCargoFail;
import static com.wuhantoc.javasample.group3.RobotStoreCargoResult.storeCargoSuccess;
import static com.wuhantoc.javasample.group3.RobotTakeOutCargoResult.takeOutFail;
import static com.wuhantoc.javasample.group3.RobotTakeOutCargoResult.takeOutSuccess;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;

abstract class AbstractLockerRobot {

    private final Collection<UserRobotAccessLocker> lockers;

    private final Map<String, UserRobotAccessLocker> ticketLockerMap;

    protected AbstractLockerRobot(Collection<? extends UserRobotAccessLocker> lockers) {
        this.lockers = new ArrayList<>(lockers.size());
        this.lockers.addAll(lockers);
        ticketLockerMap = new HashMap<>();
    }

    protected RobotStoreCargoResult storeCargo(Cargo cargo) {
        for (UserRobotAccessLocker locker : sortLockersBeforeStore(lockers)) {
            RobotStoreResult storeResult = locker.robotStore();
            if (storeResult.isSuccess()) {
                ticketLockerMap.put(storeResult.getTicket(), locker);
                storeResult.getLockerBox().store(cargo);
                return storeCargoSuccess(storeResult.getTicket(), locker);
            }
        }
        return storeCargoFail(ROBOT_STORE_FAIL_MESSAGE);
    }

    public RobotTakeOutCargoResult takeOutCargo(String ticket) {
        if (ticketLockerMap.containsKey(ticket)) {
            UserRobotAccessLocker locker = ticketLockerMap.remove(ticket);
            RobotTakeOutResult takeOutResult = locker.robotTakeOut(ticket);
            if (takeOutResult.isSuccess()) {
                return takeOutSuccess(takeOutResult.getLockerBox().get());
            }
        }
        return takeOutFail(ROBOT_TAKE_OUT_FAIL_MESSAGE);
    }

    protected abstract Collection<? extends UserRobotAccessLocker> sortLockersBeforeStore(Collection<UserRobotAccessLocker> lockers);

}
