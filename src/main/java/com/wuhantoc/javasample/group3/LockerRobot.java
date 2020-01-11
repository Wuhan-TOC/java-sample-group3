package com.wuhantoc.javasample.group3;

import java.util.Collection;

public class LockerRobot extends AbstractLockerRobot {

    public LockerRobot(Collection<UserRobotAccessLocker> lockers) {
        super(lockers);
    }

    @Override
    public RobotStoreCargoResult storeCargo(Cargo cargo) {
        return super.storeCargo(cargo);
    }

    @Override
    public RobotTakeOutCargoResult takeOutCargo(String ticket) {
        return super.takeOutCargo(ticket);
    }

    @Override
    protected Collection<UserRobotAccessLocker> sortLockersBeforeStore(Collection<UserRobotAccessLocker> lockers) {
        return lockers;
    }

}
