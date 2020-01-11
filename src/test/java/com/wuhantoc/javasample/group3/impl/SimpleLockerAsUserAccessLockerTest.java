package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserStoreResult;
import com.wuhantoc.javasample.group3.UserTakeOutResult;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Supplier;

import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.StringUtils.isNotBlank;
import static org.powermock.api.mockito.PowerMockito.mock;


class SimpleLockerAsUserAccessLockerTest {

    @Test
    void should_get_a_success_result_with_not_blank_ticket_and_not_null_locker_box_when_store_given_available_locker() {
        //given
        UserAccessLocker locker = initAvailableUserAccessLocker();
        //when
        UserStoreResult result = locker.userStore();
        //then
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(isNotBlank(result.getTicket()));
        assertNotNull(result.getLockerBox());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_store_given_full_locker() {
        //given
        UserAccessLocker locker = initFullUserAccessLocker();
        //when
        UserStoreResult result = locker.userStore();
        //then
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(USER_STORE_FAIL_MESSAGE, result.getErrorMessage());
    }

    @Test
    void should_get_a_success_result_with_the_same_locker_box_when_take_out_given_ticket_acquire_from_store() {
        //given
        UserAccessLocker locker = initAvailableUserAccessLocker();
        UserStoreResult storeResult = locker.userStore();
        String ticket = storeResult.getTicket();
        UserAccessLockerBox boxWhenStoring = storeResult.getLockerBox();
        //when
        UserTakeOutResult takeOutResult = locker.userTakeOut(ticket);
        //then
        assertNotNull(takeOutResult);
        assertTrue(takeOutResult.isSuccess());
        assertEquals(boxWhenStoring, takeOutResult.getLockerBox());
    }

    @Test
    void should_get_a_not_success_result_and_specified_error_message_when_take_out_given_ticket_different_from_any_ticket_from_store_result() {
        //given
        UserAccessLocker locker = initAvailableUserAccessLocker();
        UserStoreResult result = locker.userStore();
        String differentTicket;
        do {
            differentTicket = UUID.randomUUID().toString();
        } while (differentTicket.equals(result.getTicket()));
        //when
        UserTakeOutResult takeOutResult = locker.userTakeOut(differentTicket);
        //then
        assertNotNull(takeOutResult);
        assertFalse(takeOutResult.isSuccess());
        assertEquals(USER_TAKE_OUT_FAIL_MESSAGE, takeOutResult.getErrorMessage());
    }

    private UserAccessLocker initAvailableUserAccessLocker() {
        return SimpleLocker.initLocker(1, mockLockerBoxSupplier());
    }

    private UserAccessLocker initFullUserAccessLocker() {
        return SimpleLocker.initLocker(0, mockLockerBoxSupplier());
    }

    private Supplier<? extends UserRobotAccessLockerBox> mockLockerBoxSupplier() {
        return this::mockLockerBox;
    }

    private UserRobotAccessLockerBox mockLockerBox() {
        return mock(UserRobotAccessLockerBox.class);
    }

}
