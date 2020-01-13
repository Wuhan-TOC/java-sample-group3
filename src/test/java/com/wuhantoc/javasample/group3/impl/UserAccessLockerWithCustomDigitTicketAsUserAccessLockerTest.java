package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.UserAccessLockerTest;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
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
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(UserAccessLockerWithCustomDigitTicketAsUserAccessLockerTest.LockerTestInstanceFactory.class)
public class UserAccessLockerWithCustomDigitTicketAsUserAccessLockerTest extends UserAccessLockerTest {

    protected UserAccessLockerWithCustomDigitTicketAsUserAccessLockerTest(BiFunction<Boolean, Supplier<UserRobotAccessLockerBox>, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    @Test
    protected void should_each_user_take_out_what_the_user_stored_when_given_empty_1_digit_ticket_10_capacity_locker() {
        //given
        int times = 10;
        UserAccessLocker locker = initAvailableLocker(10, 1, lockerBoxSupplier());
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

    private static UserAccessLocker initAvailableLocker(int capacity, int digitCount, Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        return UserSuperRobotLockerWithCustomDigitTicket.initLocker(capacity, digitCount, lockerBoxSupplier);
    }

    private static UserAccessLocker initFullLocker(Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        return UserSuperRobotLockerWithCustomDigitTicket.initLocker(0, 4, lockerBoxSupplier);
    }

    static class LockerTestInstanceFactory implements TestInstanceFactory {

        @Override
        public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
            BiFunction<Boolean, Supplier<UserRobotAccessLockerBox>, UserAccessLocker> lockerProvider = (fullLocker, lockerBoxSupplier) -> {
                if (fullLocker) {
                    return initFullLocker(lockerBoxSupplier);
                }
                return initAvailableLocker(1, 4, lockerBoxSupplier);
            };
            return newInstance(factoryContext, lockerProvider);
        }

    }
}
