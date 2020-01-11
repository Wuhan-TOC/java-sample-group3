package com.wuhantoc.javasample.group3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wuhantoc.javasample.group3.RobotStoreCargoResult.storeCargoFail;
import static com.wuhantoc.javasample.group3.RobotStoreCargoResult.storeCargoSuccess;
import static com.wuhantoc.javasample.group3.RobotTakeoutCargoResult.takeOutFail;
import static com.wuhantoc.javasample.group3.RobotTakeoutCargoResult.takeOutSuccess;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;

public class SuperLockerRobot {

    private final Collection<UserSuperRobotAccessLocker> lockers;

    private final Map<String, UserSuperRobotAccessLocker> ticketLockerMap;

    public SuperLockerRobot(Collection<UserSuperRobotAccessLocker> lockers) {
        this.lockers = lockers;
        ticketLockerMap = new HashMap<>();
    }

    public RobotStoreCargoResult storeCargo(Cargo cargo) {
        Collection<UserSuperRobotAccessLocker> sortedLockers = lockers.stream()
                .filter(userRobotAccessLocker -> Double.compare(userRobotAccessLocker.getEmptyRate(), 0) > 0)
                .sorted((locker1, locker2) -> Double.compare(locker2.getEmptyRate(), locker1.getEmptyRate()))
                .collect(Collectors.toList());
        for (UserSuperRobotAccessLocker locker : sortedLockers) {
            RobotStoreResult storeResult = locker.storeCargo();
            if (storeResult.isSuccess()) {
                ticketLockerMap.put(storeResult.getTicket(), locker);
                storeResult.getLockerBox().store(cargo);
                return storeCargoSuccess(storeResult.getTicket(), locker);
            }
        }
        return storeCargoFail(ROBOT_STORE_FAIL_MESSAGE);
    }

    public RobotTakeoutCargoResult takeOutCargo(String ticket) {
        if (ticketLockerMap.containsKey(ticket)) {
            UserRobotAccessLocker locker = ticketLockerMap.remove(ticket);
            RobotTakeOutResult takeOutResult = locker.takeOutCargo(ticket);
            if (takeOutResult.isSuccess()) {
                return takeOutSuccess(takeOutResult.getLockerBox().get());
            }
        }
        return takeOutFail(ROBOT_TAKE_OUT_FAIL_MESSAGE);
    }

}
