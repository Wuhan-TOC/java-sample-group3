package com.wuhantoc.javasample.group3;

import java.util.Collection;
import java.util.stream.Collectors;

public class SuperLockerRobot extends AbstractLockerRobot {

    public SuperLockerRobot(Collection<UserSuperRobotAccessLocker> lockers) {
        super(lockers);
    }

    @Override
    public RobotStoreCargoResult storeCargo(Cargo cargo) {
        return super.storeCargo(cargo);
    }

    @Override
    public RobotTakeOutCargoResult takeOutCargo(String robotTicket) {
        return super.takeOutCargo(robotTicket);
    }

    @Override
    protected Collection<UserSuperRobotAccessLocker> sortLockersBeforeStore(Collection<UserRobotAccessLocker> lockers) {
        return lockers.stream()
                .map(locker -> (UserSuperRobotAccessLocker) locker)
                .filter(userRobotAccessLocker -> Double.compare(userRobotAccessLocker.getEmptyRate(), 0) > 0)
                .sorted((locker1, locker2) -> Double.compare(locker2.getEmptyRate(), locker1.getEmptyRate()))
                .collect(Collectors.toList());
    }


}
