package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.locker.Locker;
import com.wuhantoc.javasample.group3.locker.LockerStoreResult;
import com.wuhantoc.javasample.group3.locker.LockerTakeOutResult;
import com.wuhantoc.javasample.group3.lockerbox.LockerBox;
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


class LockerTest {

    @Test
    void should_get_a_success_result_with_not_blank_ticket_and_not_null_locker_box_when_user_store_given_available_locker() {
        //given
        Locker locker = initAvailableLocker();
        //when
        LockerStoreResult result = locker.store();
        //then
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertTrue(isNotBlank(result.getTicket()));
        assertNotNull(result.getLockerBox());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_user_store_given_full_locker() {
        //given
        Locker locker = initFullLocker();
        //when
        LockerStoreResult result = locker.store();
        //then
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(USER_STORE_FAIL_MESSAGE, result.getErrorMessage());
    }

    @Test
    void should_get_a_success_result_with_the_same_locker_box_when_user_take_out_given_ticket_acquire_from_store() {
        //given
        Locker locker = initAvailableLocker();
        LockerStoreResult storeResult = locker.store();
        String ticket = storeResult.getTicket();
        LockerBox boxWhenStoring = storeResult.getLockerBox();
        //when
        LockerTakeOutResult takeOutResult = locker.takeOut(ticket);
        //then
        assertNotNull(takeOutResult);
        assertTrue(takeOutResult.isSuccess());
        assertEquals(boxWhenStoring, takeOutResult.getLockerBox());
    }

    @Test
    void should_get_a_not_success_result_and_specified_error_message_when_user_take_out_given_ticket_different_from_any_ticket_from_store_result() {
        //given
        Locker locker = initAvailableLocker();
        LockerStoreResult result = locker.store();
        String differentTicket;
        do {
            differentTicket = UUID.randomUUID().toString();
        } while (differentTicket.equals(result.getTicket()));
        //when
        LockerTakeOutResult takeOutResult = locker.takeOut(differentTicket);
        //then
        assertNotNull(takeOutResult);
        assertFalse(takeOutResult.isSuccess());
        assertEquals(USER_TAKE_OUT_FAIL_MESSAGE, takeOutResult.getErrorMessage());
    }

    @Test
    void should_get_0_vacancy_rate_when_get_vacancy_rate_give_an_0_capacity_locker() {
        //given
        int capacity = 0;
        Locker locker = initEmptyLocker(capacity);
        //when & then
        assertEquals(0, locker.getVacancyRate());
    }

    @Test
    void should_get_1_vacancy_rate_when_get_vacancy_rate_give_an_24_capacity_locker() {
        //given
        int capacity = 24;
        Locker locker = initEmptyLocker(capacity);
        //when & then
        assertEquals(1, locker.getVacancyRate());
    }

    @Test
    void should_get_0_point_5_vacancy_rate_when_get_vacancy_rate_give_an_24_capacity_locker_stored_12_times() {
        //given
        int capacity = 24;
        int times = 12;
        Locker locker = initEmptyLocker(capacity);
        for (int i = 0; i < times; i++) {
            locker.store();
        }
        //when & then
        assertEquals(0.5, locker.getVacancyRate());
    }

    private Locker initEmptyLocker(int capacity) {
        return Locker.initLocker(capacity, mockLockerBoxSupplier());
    }

    private Locker initAvailableLocker() {
        return initEmptyLocker(1);
    }

    private Locker initFullLocker() {
        return initEmptyLocker(0);
    }

    private Supplier<LockerBox> mockLockerBoxSupplier() {
        return this::mockLockerBox;
    }

    private LockerBox mockLockerBox() {
        return mock(LockerBox.class);
    }

}
