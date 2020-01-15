package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.locker.Locker;
import com.wuhantoc.javasample.group3.lockerbox.LockerBox;
import com.wuhantoc.javasample.group3.lockerrobot.LockerRobot;
import com.wuhantoc.javasample.group3.lockerrobot.RobotStoreResult;
import com.wuhantoc.javasample.group3.lockerrobot.RobotTakeOutResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.locker.LockerStoreResult.storeLockerFail;
import static com.wuhantoc.javasample.group3.locker.LockerStoreResult.storeLockerSuccess;
import static com.wuhantoc.javasample.group3.locker.LockerTakeOutResult.takeOutLockerFail;
import static com.wuhantoc.javasample.group3.locker.LockerTakeOutResult.takeOutLockerSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class LockerRobotTest {

    private static final Cargo ANY_CARGO = mock(Cargo.class);

    private static final String MOCK_TICKET = "";

    @Test
    void should_save_in_locker1_when_save_package_given_robot_with_empty_locker1_full_locker2_and_empty_locker3() {
        //mock
        Locker locker1 = mockAvailableLocker();
        Locker locker2 = mockFullLocker();
        Locker locker3 = mockAvailableLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker1, locker2, locker3);
        //when
        RobotStoreResult result = lockerRobot.storeCargo(ANY_CARGO);
        //then
        assertTrue(result.isSuccess());
        assertNotNull(result.getRobotTicket());
        assertNotNull(result.getLockerTicket());
        assertEquals(locker1, result.getLocker());
    }

    @Test
    void should_a_not_success_result_with_specified_error_message_when_store_cargo_given_a_robot_with_full_locker1_and_full_locker2() {
        //mock
        Locker locker1 = mockFullLocker();
        Locker locker2 = mockFullLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker1, locker2);
        //when
        RobotStoreResult result = lockerRobot.storeCargo(ANY_CARGO);
        //then
        assertFalse(result.isSuccess());
        assertEquals(ROBOT_STORE_FAIL_MESSAGE, result.getErrorMessage());
    }

    @Test
    void should_get_stored_cargo_when_take_out_cargo_given_a_robot_and_ticket_acquire_from_store_cargo() {
        //mock
        Locker locker = mockAvailableLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreResult storeCargoResult = lockerRobot.storeCargo(ANY_CARGO);
        String storeCargoTicket = storeCargoResult.getRobotTicket();
        //when
        RobotTakeOutResult takeoutCargoResult = lockerRobot.takeOutCargo(storeCargoTicket);
        //then
        assertTrue(takeoutCargoResult.isSuccess());
        assertEquals(ANY_CARGO, takeoutCargoResult.getCargo());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_take_out_cargo_given_a_robot_and_ticket_different_from_any_ticket_from_store_cargo_result() {
        //mock
        Locker locker = mockAvailableLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreResult result = lockerRobot.storeCargo(ANY_CARGO);
        String differentTicket;
        do {
            differentTicket = UUID.randomUUID().toString();
        } while (differentTicket.equals(result.getRobotTicket()));
        //when
        RobotTakeOutResult takeoutCargoResult = lockerRobot.takeOutCargo(differentTicket);
        //then
        assertFalse(takeoutCargoResult.isSuccess());
        assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, takeoutCargoResult.getErrorMessage());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_take_out_cargo_given_a_robot_and_used_ticket() {
        //mock
        Locker locker = mockUserTookOutLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreResult storeCargoResult = lockerRobot.storeCargo(ANY_CARGO);
        storeCargoResult.getLocker().takeOut(storeCargoResult.getLockerTicket());
        //when
        RobotTakeOutResult takeoutCargoResult = lockerRobot.takeOutCargo(storeCargoResult.getRobotTicket());
        //then
        assertFalse(takeoutCargoResult.isSuccess());
        assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, takeoutCargoResult.getErrorMessage());
    }

    private LockerRobot initRobotWithGivenLockers(Locker... lockers) {
        return new LockerRobot(Arrays.asList(lockers));
    }

    private LockerBox mockLockerBox() {
        LockerBox lockerBox = mock(LockerBox.class);
        when(lockerBox.get()).thenReturn(ANY_CARGO);
        return lockerBox;
    }

    private Locker mockAvailableLocker() {
        String mockTicket = MOCK_TICKET;
        LockerBox lockerBox = mockLockerBox();
        Locker locker = mock(Locker.class);
        when(locker.store()).thenReturn(storeLockerSuccess(mockTicket, lockerBox));
        when(locker.takeOut(mockTicket)).thenReturn(takeOutLockerSuccess(lockerBox));
        return locker;
    }

    private Locker mockFullLocker() {
        Locker locker = mock(Locker.class);
        when(locker.store()).thenReturn(storeLockerFail(USER_STORE_FAIL_MESSAGE));
        return locker;
    }

    private Locker mockUserTookOutLocker() {
        String mockTicket = MOCK_TICKET;
        LockerBox lockerBox = mockLockerBox();
        Locker locker = mock(Locker.class);
        when(locker.store()).thenReturn(storeLockerSuccess(mockTicket, lockerBox));
        when(locker.takeOut(mockTicket)).thenReturn(takeOutLockerSuccess(lockerBox), takeOutLockerFail(USER_TAKE_OUT_FAIL_MESSAGE));
        return locker;
    }

}
