package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.util.function.Function;

@ExtendWith(UserSuperRobotLockerAsUserAccessLockerWithUUIDTicketTest.LockerTestInstanceFactory.class)
class UserSuperRobotLockerAsUserAccessLockerWithUUIDTicketTest extends UserAccessLockerTest {

    UserSuperRobotLockerAsUserAccessLockerWithUUIDTicketTest(Function<Boolean, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    private static UserAccessLocker initAvailableLocker() {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(1, mockLockerBoxSupplier());
    }

    private static UserAccessLocker initFullLocker() {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(0, mockLockerBoxSupplier());
    }

    static class LockerTestInstanceFactory implements TestInstanceFactory {

        @Override
        public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
            return new UserSuperRobotLockerAsUserAccessLockerWithUUIDTicketTest(fullLocker -> {
                if (fullLocker) {
                    return initFullLocker();
                }
                return initAvailableLocker();
            });
        }

    }
}
