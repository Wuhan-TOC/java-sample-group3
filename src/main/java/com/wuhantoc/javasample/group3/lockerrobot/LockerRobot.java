package com.wuhantoc.javasample.group3.lockerrobot;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.locker.Locker;

import java.util.Collection;

public class LockerRobot extends AbstractLockerRobot {

    public LockerRobot(Collection<Locker> lockers) {
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
        return lockers;
    }

}
