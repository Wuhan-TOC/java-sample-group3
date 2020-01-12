package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.UserAccessLockerTest;
import com.wuhantoc.javasample.group3.UserStoreResult;
import com.wuhantoc.javasample.group3.UserTakeOutResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(UserAccessLockerWithCustomDigitTicketTest.LockerTestInstanceFactory.class)
class UserAccessLockerWithCustomDigitTicketTest extends UserAccessLockerTest {

    UserAccessLockerWithCustomDigitTicketTest(Function<Boolean, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    @Test
    void should_each_user_take_out_what_the_user_stored_when_given_empty_1_digit_ticket_10_capacity_locker() {
        //given
        int times = 10;
        UserAccessLocker locker = UserSuperRobotLockerWithCustomDigitTicket.initLocker(10, 1, mockLockerBoxSupplier());
        Map<String, UserAccessLockerBox> ticketLockerMap = new HashMap<>(times);
        for (int i = 0; i < times; i++) {
            UserStoreResult result = locker.userStore();
            assertTrue(result.isSuccess());
            ticketLockerMap.put(result.getTicket(), result.getLockerBox());
        }
        //when & then
        ticketLockerMap.forEach((ticket, lockerBox) -> {
            UserTakeOutResult takeOutResult = locker.userTakeOut(ticket);
            assertTrue(takeOutResult.isSuccess());
            assertEquals(lockerBox, takeOutResult.getLockerBox());
        });
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
