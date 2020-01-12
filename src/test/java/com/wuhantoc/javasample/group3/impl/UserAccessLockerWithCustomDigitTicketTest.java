package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerTest;
import com.wuhantoc.javasample.group3.UserStoreResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.StringUtils.isNotBlank;

@ExtendWith(UserAccessLockerWithCustomDigitTicketTest.LockerTestInstanceFactory.class)
class UserAccessLockerWithCustomDigitTicketTest extends UserAccessLockerTest {

    UserAccessLockerWithCustomDigitTicketTest(Function<Boolean, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    @Test
    void should_success_all_10_store_when_user_store_given_10_capacity_empty_1_digit_ticket_locker() {
        //given
        int times = 10;
        UserAccessLocker locker = UserSuperRobotLockerWithCustomDigitTicket.initLocker(10, 1, mockLockerBoxSupplier());
        //when & then
        for (int i = 0; i < times; i++) {
            UserStoreResult result = locker.userStore();
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertTrue(isNotBlank(result.getTicket()));
            assertNotNull(result.getLockerBox());
        }
    }

    private static UserAccessLocker initAvailableLocker() {
        return UserSuperRobotLockerWithCustomDigitTicket.initLocker(1, 4, mockLockerBoxSupplier());
    }

    private static UserAccessLocker initFullLocker() {
        return UserSuperRobotLockerWithCustomDigitTicket.initLocker(0, 4, mockLockerBoxSupplier());
    }

    static class LockerTestInstanceFactory implements TestInstanceFactory {

        @Override
        public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
            return new UserAccessLockerWithCustomDigitTicketTest(fullLocker -> {
                if (fullLocker) {
                    return initFullLocker();
                }
                return initAvailableLocker();
            });
        }

    }
}
