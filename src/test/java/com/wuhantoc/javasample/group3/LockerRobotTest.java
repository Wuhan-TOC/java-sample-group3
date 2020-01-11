package com.wuhantoc.javasample.group3;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.wuhantoc.javasample.group3.RobotStoreResult.storeFail;
import static com.wuhantoc.javasample.group3.RobotStoreResult.storeSuccess;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.takeOutFail;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.takeOutSuccess;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
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
        UserRobotAccessLocker locker1 = mockAvailableLocker();
        UserRobotAccessLocker locker2 = mockFullLocker();
        UserRobotAccessLocker locker3 = mockAvailableLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker1, locker2, locker3);
        //when
        RobotStoreCargoResult result = lockerRobot.storeCargo(ANY_CARGO);
        //then
        assertTrue(result.isSuccess());
        assertNotNull(result.getTicket());
        assertEquals(locker1, result.getLocker());
    }

    @Test
    void should_a_not_success_result_with_specified_error_message_when_store_cargo_given_a_robot_with_full_locker1_and_full_locker2() {
        //mock
        UserRobotAccessLocker locker1 = mockFullLocker();
        UserRobotAccessLocker locker2 = mockFullLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker1, locker2);
        //when
        RobotStoreCargoResult result = lockerRobot.storeCargo(ANY_CARGO);
        //then
        assertFalse(result.isSuccess());
        assertEquals(ROBOT_STORE_FAIL_MESSAGE, result.getErrorMessage());
    }

    @Test
    void should_get_stored_cargo_when_take_out_cargo_given_a_robot_and_ticket_acquire_from_store_cargo() {
        //mock
        UserRobotAccessLocker locker = mockAvailableLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreCargoResult storeCargoResult = lockerRobot.storeCargo(ANY_CARGO);
        String storeCargoTicket = storeCargoResult.getTicket();
        //when
        RobotTakeoutCargoResult takeoutCargoResult = lockerRobot.takeOutCargo(storeCargoTicket);
        //then
        assertTrue(takeoutCargoResult.isSuccess());
        assertEquals(ANY_CARGO, takeoutCargoResult.getCargo());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_take_out_cargo_given_a_robot_and_ticket_different_from_any_ticket_from_store_cargo_result() {
        //mock
        UserRobotAccessLocker locker = mockAvailableLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreCargoResult result = lockerRobot.storeCargo(ANY_CARGO);
        String differentTicket;
        do {
            differentTicket = UUID.randomUUID().toString();
        } while (differentTicket.equals(result.getTicket()));
        //when
        RobotTakeoutCargoResult takeoutCargoResult = lockerRobot.takeOutCargo(differentTicket);
        //then
        assertFalse(takeoutCargoResult.isSuccess());
        assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, takeoutCargoResult.getErrorMessage());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_take_out_cargo_given_a_robot_and_used_ticket() {
        //mock
        UserRobotAccessLocker locker = mockUserTookOutLocker();
        //given
        LockerRobot lockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreCargoResult storeCargoResult = lockerRobot.storeCargo(ANY_CARGO);
        String storeCargoTicket = storeCargoResult.getTicket();
        storeCargoResult.getLocker().takeOut(storeCargoTicket);
        //when
        RobotTakeoutCargoResult takeoutCargoResult = lockerRobot.takeOutCargo(storeCargoTicket);
        //then
        assertFalse(takeoutCargoResult.isSuccess());
        assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, takeoutCargoResult.getErrorMessage());
    }

    private LockerRobot initRobotWithGivenLockers(UserRobotAccessLocker... lockers) {
        return new LockerRobot(Arrays.asList(lockers));
    }

    private UserRobotAccessLockerBox mockLockerBox() {
        UserRobotAccessLockerBox lockerBox = mock(UserRobotAccessLockerBox.class);
        when(lockerBox.get()).thenReturn(ANY_CARGO);
        return lockerBox;
    }

    private UserRobotAccessLocker mockAvailableLocker() {
        String mockTicket = MOCK_TICKET;
        UserRobotAccessLockerBox lockerBox = mockLockerBox();
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        when(locker.takeOutCargo(mockTicket)).thenReturn(takeOutSuccess(lockerBox));
        return locker;
    }

    private UserRobotAccessLocker mockFullLocker() {
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.storeCargo()).thenReturn(storeFail(USER_STORE_FAIL_MESSAGE));
        return locker;
    }

    private UserRobotAccessLocker mockUserTookOutLocker() {
        String mockTicket = MOCK_TICKET;
        UserRobotAccessLockerBox lockerBox = mockLockerBox();
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        when(locker.takeOutCargo(mockTicket)).thenReturn(takeOutFail(USER_TAKE_OUT_FAIL_MESSAGE));
        return locker;
    }

}
