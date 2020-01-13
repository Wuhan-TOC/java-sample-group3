package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.impl.SimpleUserAccessLockerBox;
import com.wuhantoc.javasample.group3.impl.UserSuperRobotLockerWithUUIDTicket;

import java.util.function.Supplier;

public class UserUsingLockerRobotWithUUIDTicketLockerTest extends LockerRobotTest {


    @Override
    protected UserRobotAccessLocker initAvailableLocker() {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(10, simpleLockerBoxSupplier());
    }

    @Override
    protected UserRobotAccessLocker initFullLocker() {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(0, simpleLockerBoxSupplier());
    }

    protected static Supplier<UserRobotAccessLockerBox> simpleLockerBoxSupplier() {
        return SimpleUserAccessLockerBox::new;
    }

}
