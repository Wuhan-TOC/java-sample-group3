package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.impl.SimpleUserAccessLockerBox;
import com.wuhantoc.javasample.group3.impl.UserAccessLockerWithCustomDigitTicketAsUserAccessLockerTest;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class UserUsingLockerWithCustomDigitTicketLockerTest extends UserAccessLockerWithCustomDigitTicketAsUserAccessLockerTest {

    protected UserUsingLockerWithCustomDigitTicketLockerTest(BiFunction<Boolean, Supplier<UserRobotAccessLockerBox>, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    @Override
    protected Supplier<UserRobotAccessLockerBox> lockerBoxSupplier() {
        return SimpleUserAccessLockerBox::new;
    }

}
