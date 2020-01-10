package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.RobotAccessLocker;
import com.wuhantoc.javasample.group3.RobotAccessLockerBox;
import com.wuhantoc.javasample.group3.RobotStoreCargoResult;
import com.wuhantoc.javasample.group3.RobotTakeOutResult;
import com.wuhantoc.javasample.group3.RobotTakeoutCargoResult;
import com.wuhantoc.javasample.group3.SuperLockerRobot;
import com.wuhantoc.javasample.group3.UserRobotAccessLocker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.wuhantoc.javasample.group3.RobotStoreResult.storeSuccess;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.takeOutSuccess;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class SuperLockerRobotTest {

    private static final Cargo ANY_CARGO = mock(Cargo.class);

    @Test
    void should_store_in_locker1_when_store_cargo_given_a_robot_with_100_empty_locker1_and_50_empty_locker2() {
        //mock
        String mockTicket = "mock ticket";
        RobotAccessLockerBox lockerBox = mock(RobotAccessLockerBox.class);
        UserRobotAccessLocker locker1 = mock(UserRobotAccessLocker.class);
        when(locker1.getEmptyRate()).thenReturn(1.0);
        when(locker1.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        UserRobotAccessLocker locker2 = mock(UserRobotAccessLocker.class);
        when(locker2.getEmptyRate()).thenReturn(0.5);
        //given
        SuperLockerRobot superLockerRobot = initRobotWithGivenLockers(locker1, locker2);
        //when
        RobotStoreCargoResult result = superLockerRobot.storeCargo(ANY_CARGO);
        //then
        assertTrue(result.isSuccess());
        assertNotNull(result.getTicket());
        assertEquals(locker1, result.getLocker());
    }

    @Test
    void should_store_in_locker1_when_store_cargo_given_a_robot_with_50_empty_locker1_and_50_empty_locker2() {
        //mock
        String mockTicket = "mock ticket";
        RobotAccessLockerBox lockerBox = mock(RobotAccessLockerBox.class);
        UserRobotAccessLocker locker1 = mock(UserRobotAccessLocker.class);
        when(locker1.getEmptyRate()).thenReturn(0.5);
        when(locker1.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        UserRobotAccessLocker locker2 = mock(UserRobotAccessLocker.class);
        when(locker2.getEmptyRate()).thenReturn(0.5);
        //given
        SuperLockerRobot superLockerRobot = initRobotWithGivenLockers(locker1, locker2);
        //when
        RobotStoreCargoResult result = superLockerRobot.storeCargo(ANY_CARGO);
        //then
        assertTrue(result.isSuccess());
        assertNotNull(result.getTicket());
        assertEquals(locker1, result.getLocker());
    }

    @Test
    void should_get_a_not_success_result_with_specified_error_message_when_store_cargo_given_a_robot_with_0_empty_locker1_and_0_empty_locker2() {
        //mock
        UserRobotAccessLocker locker1 = mock(UserRobotAccessLocker.class);
        when(locker1.getEmptyRate()).thenReturn(0.0);
        UserRobotAccessLocker locker2 = mock(UserRobotAccessLocker.class);
        when(locker2.getEmptyRate()).thenReturn(0.0);
        //given
        SuperLockerRobot superLockerRobot = initRobotWithGivenLockers(locker1, locker2);
        //when
        RobotStoreCargoResult result = superLockerRobot.storeCargo(ANY_CARGO);
        //then
        assertFalse(result.isSuccess());
        assertEquals(ROBOT_STORE_FAIL_MESSAGE, result.getErrorMessage());
    }

    @Test
    void should_get_stored_cargo_when_take_out_cargo_given_a_robot_and_ticket_acquire_from_store_cargo() {
        //mock
        String mockTicket = "mock ticket";
        RobotAccessLockerBox lockerBox = mock(RobotAccessLockerBox.class);
        when(lockerBox.get()).thenReturn(ANY_CARGO);
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.getEmptyRate()).thenReturn(1.0);
        when(locker.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        when(locker.takeOutCargo(anyString())).thenReturn(takeOutSuccess(lockerBox));
        //given
        SuperLockerRobot superLockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreCargoResult storeCargoResult = superLockerRobot.storeCargo(ANY_CARGO);
        String storeCargoTicket = storeCargoResult.getTicket();
        //when
        RobotTakeoutCargoResult takeoutCargoResult = superLockerRobot.takeOutCargo(storeCargoTicket);
        //then
        assertTrue(takeoutCargoResult.isSuccess());
        assertEquals(ANY_CARGO, takeoutCargoResult.getCargo());
    }

    @Test
    void should_a_not_success_result_with_specified_error_message_when_take_out_cargo_given_a_robot_and_ticket_different_from_any_ticket_from_store_cargo_result() {
        //mock
        String mockTicket = "mock ticket";
        RobotAccessLockerBox lockerBox = mock(RobotAccessLockerBox.class);
        when(lockerBox.get()).thenReturn(ANY_CARGO);
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        //given
        SuperLockerRobot superLockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreCargoResult result = superLockerRobot.storeCargo(ANY_CARGO);
        String differentTicket;
        do {
            differentTicket = UUID.randomUUID().toString();
        } while (differentTicket.equals(result.getTicket()));
        //when
        RobotTakeoutCargoResult takeoutCargoResult = superLockerRobot.takeOutCargo(differentTicket);
        //then
        assertFalse(takeoutCargoResult.isSuccess());
        assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, takeoutCargoResult.getErrorMessage());
    }

    @Test
    void should_a_not_success_result_with_specified_error_message_when_take_out_cargo_given_a_robot_and_used_ticket() {
        //mock
        String mockTicket = "mock ticket";
        RobotAccessLockerBox lockerBox = mock(RobotAccessLockerBox.class);
        when(lockerBox.get()).thenReturn(ANY_CARGO);
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.getEmptyRate()).thenReturn(1.0);
        when(locker.storeCargo()).thenReturn(storeSuccess(mockTicket, lockerBox));
        when(locker.takeOutCargo(anyString())).thenReturn(RobotTakeOutResult.takeOutFail(ROBOT_TAKE_OUT_FAIL_MESSAGE));
        //given
        SuperLockerRobot superLockerRobot = initRobotWithGivenLockers(locker);
        RobotStoreCargoResult storeCargoResult = superLockerRobot.storeCargo(ANY_CARGO);
        String storeCargoTicket = storeCargoResult.getTicket();
        storeCargoResult.getLocker().takeOut(storeCargoTicket);
        //when
        RobotTakeoutCargoResult takeoutCargoResult = superLockerRobot.takeOutCargo(storeCargoTicket);
        //then
        assertFalse(takeoutCargoResult.isSuccess());
        assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, takeoutCargoResult.getErrorMessage());
    }

    private SuperLockerRobot initRobotWithGivenLockers(UserRobotAccessLocker... lockers) {
        return new SuperLockerRobot(Arrays.asList(lockers));
    }

}
