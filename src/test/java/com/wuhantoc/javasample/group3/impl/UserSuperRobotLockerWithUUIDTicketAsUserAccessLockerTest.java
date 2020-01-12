package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerTest;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@ExtendWith(UserSuperRobotLockerWithUUIDTicketAsUserAccessLockerTest.LockerTestInstanceFactory.class)
public class UserSuperRobotLockerWithUUIDTicketAsUserAccessLockerTest extends UserAccessLockerTest {

    protected UserSuperRobotLockerWithUUIDTicketAsUserAccessLockerTest(BiFunction<Boolean, Supplier<UserRobotAccessLockerBox>, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    private static UserAccessLocker initAvailableLocker(Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(1, lockerBoxSupplier);
    }

    private static UserAccessLocker initFullLocker(Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(0, lockerBoxSupplier);
    }

    protected static class LockerTestInstanceFactory implements TestInstanceFactory {

        @Override
        public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
            BiFunction<Boolean, Supplier<UserRobotAccessLockerBox>, UserAccessLocker> lockerProvider = (fullLocker, lockerBoxSupplier) -> {
                if (fullLocker) {
                    return initFullLocker(lockerBoxSupplier);
                }
                return initAvailableLocker(lockerBoxSupplier);
            };
            return newInstance(factoryContext, lockerProvider);
        }

    }
}
