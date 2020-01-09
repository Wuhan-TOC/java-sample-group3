package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.impl.UUIDLocker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.wuhantoc.javasample.group3.Constant.MUST_BE_WRONG_TICKET;
import static com.wuhantoc.javasample.group3.LockerRobot.LOCKERS_ARE_ALL_FULL_MESSAGES;
import static com.wuhantoc.javasample.group3.LockerRobot.USED_TICKET_MESSAGE;
import static com.wuhantoc.javasample.group3.LockerRobot.WRONG_TICKET_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LockerRobotTest {

    private Locker firstLocker;

    private Locker lastLocker;

    private Locker middleLocker;

    private LockerRobot robot;

    private String ticket;

    @Test
    void should_save_in_the_first_locker_when_save_package_given_robot_with_middle_full_lockers() {
        // given
        initRobotWithMiddleFullLockers();
        // when
        RobotSavePackageResult result = robot.savePackage();
        // then
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTicket());
        assertNull(result.getErrorMessage());
        assertEquals(firstLocker, result.getLocker());
    }

    private void initRobotWithMiddleFullLockers() {
        firstLocker = new UUIDLocker(1);
        middleLocker = new UUIDLocker(0);
        lastLocker = new UUIDLocker(1);
        List<Locker> lockers = new ArrayList<>();
        lockers.add(firstLocker);
        lockers.add(middleLocker);
        lockers.add(lastLocker);
        robot = new LockerRobot(lockers);
    }

    @Test
    void should_get_error_message_when_save_package_given_robot_with_all_full_lockers() {
        // given
        initRobotWithAllFullLockers();
        // when
        RobotSavePackageResult result = robot.savePackage();
        // then
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getTicket());
        assertEquals(LOCKERS_ARE_ALL_FULL_MESSAGES, result.getErrorMessage());
        assertNull(result.getLocker());
    }

    private void initRobotWithAllFullLockers() {
        firstLocker = new UUIDLocker(0);
        middleLocker = new UUIDLocker(0);
        lastLocker = new UUIDLocker(0);
        List<Locker> lockers = new ArrayList<>();
        lockers.add(firstLocker);
        lockers.add(middleLocker);
        lockers.add(lastLocker);
        robot = new LockerRobot(lockers);
    }

    @Test
    void should_success_when_get_package_given_correct_ticket() {
        // given
        initCorrectTicket();
        // when
        GetPackageResult result = robot.getPackage(ticket);
        // then
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNull(result.getErrorMessage());
    }

    private void initCorrectTicket() {
        firstLocker = new UUIDLocker(1);
        middleLocker = new UUIDLocker(1);
        lastLocker = new UUIDLocker(1);
        List<Locker> lockers = new ArrayList<>();
        lockers.add(firstLocker);
        lockers.add(middleLocker);
        lockers.add(lastLocker);
        robot = new LockerRobot(lockers);
        ticket = robot.savePackage().getTicket();
    }

    @Test
    void should_get_the_error_massage_when_get_package_given_wrong_ticket() {
        // given
        initWrongTicket();
        // when
        GetPackageResult result = robot.getPackage(ticket);
        // then
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(WRONG_TICKET_MESSAGE, result.getErrorMessage());
    }

    private void initWrongTicket() {
        firstLocker = new UUIDLocker(1);
        middleLocker = new UUIDLocker(1);
        lastLocker = new UUIDLocker(1);
        List<Locker> lockers = new ArrayList<>();
        lockers.add(firstLocker);
        lockers.add(middleLocker);
        lockers.add(lastLocker);
        robot = new LockerRobot(lockers);
        ticket = MUST_BE_WRONG_TICKET;
    }

    @Test
    void should_get_the_error_massage_when_get_package_given_used_ticket() {
        // given
        initUsedTicket();
        // when
        GetPackageResult result = robot.getPackage(ticket);
        // then
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(USED_TICKET_MESSAGE, result.getErrorMessage());
    }

    private void initUsedTicket() {
        firstLocker = new UUIDLocker(1);
        middleLocker = new UUIDLocker(1);
        lastLocker = new UUIDLocker(1);
        List<Locker> lockers = new ArrayList<>();
        lockers.add(firstLocker);
        lockers.add(middleLocker);
        lockers.add(lastLocker);
        robot = new LockerRobot(lockers);
        RobotSavePackageResult savePackageResult = robot.savePackage();
        ticket = savePackageResult.getTicket();
        savePackageResult.getLocker().getPackage(ticket);
    }

}
