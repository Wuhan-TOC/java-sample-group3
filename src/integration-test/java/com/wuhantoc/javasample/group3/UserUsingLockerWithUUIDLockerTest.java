package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.impl.SimpleUserAccessLockerBox;
import com.wuhantoc.javasample.group3.impl.UserAccessLockerWithCustomDigitTicketAsUserAccessLockerTest;
import com.wuhantoc.javasample.group3.impl.UserSuperRobotLockerWithCustomDigitTicket;
import com.wuhantoc.javasample.group3.impl.UserSuperRobotLockerWithUUIDTicket;
import com.wuhantoc.javasample.group3.impl.UserSuperRobotLockerWithUUIDTicketAsUserAccessLockerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.StringUtils.isNotBlank;

public class UserUsingLockerWithUUIDLockerTest extends UserSuperRobotLockerWithUUIDTicketAsUserAccessLockerTest {

    protected UserUsingLockerWithUUIDLockerTest(BiFunction<Boolean, Supplier<UserRobotAccessLockerBox>, UserAccessLocker> lockerProvider) {
        super(lockerProvider);
    }

    @Test
    void should_get_a_success_result_with_not_blank_ticket_when_user_store_given_an_available_locker_changed_from_full() {
        //given
        int capacity = 10;
        String[] tickets = new String[capacity];
        UserAccessLocker locker = initAvailableLocker(capacity);
        for (int i = 0; i < capacity; i++) {
            tickets[i] = requireNonNull(locker.userStore().getTicket());
        }
        for (String ticket : tickets) {
            locker.userTakeOut(ticket);
        }
        //when
        UserStoreResult result = locker.userStore();
        //then
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(isNotBlank(result.getTicket()));
        assertNotNull(result.getLockerBox());
    }

    protected static Supplier<UserRobotAccessLockerBox> simpleLockerBoxSupplier() {
        return SimpleUserAccessLockerBox::new;
    }

    private static UserAccessLocker initAvailableLocker(int capacity) {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(capacity, simpleLockerBoxSupplier());
    }

    private static UserAccessLocker initFullLocker() {
        return UserSuperRobotLockerWithUUIDTicket.initLocker(0, simpleLockerBoxSupplier());
    }

}
