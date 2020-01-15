package com.wuhantoc.javasample.group3.lockerrobot;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.locker.Locker;

import java.util.Collection;
import java.util.stream.Collectors;

public class SuperLockerRobot extends AbstractLockerRobot {

    public SuperLockerRobot(Collection<Locker> lockers) {
        super(lockers);
    }

    @Override
    public RobotStoreResult storeCargo(Cargo cargo) {
        return super.storeCargo(cargo);
    }

    @Override
    public RobotTakeOutResult takeOutCargo(String robotTicket) {
        return super.takeOutCargo(robotTicket);
    }

    @Override
    protected Collection<Locker> sortLockersBeforeStore(Collection<Locker> lockers) {
        return lockers.stream()
                .filter(userRobotAccessLocker -> Double.compare(userRobotAccessLocker.getVacancyRate(), 0) > 0)
                .sorted((locker1, locker2) -> Double.compare(locker2.getVacancyRate(), locker1.getVacancyRate()))
                .collect(Collectors.toList());
    }


}
