package com.wuhantoc.javasample.group3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LockerRobotTest {

    private Locker firstLocker;

    private Locker lastLocker;

    private Locker middleLocker;

    private LockerRobot robot;

    @Test
    void should_save_in_the_first_locker_when_save_package_given_robot_with_middle_full_lockers() {
        // given
        initRobotWithAllEmptyLockers();
        // when
        RobotSavePackageResult result = robot.savePackage();
        // then
        assertNotNull(result);
        LockerSavePackageResult lockerResult = result.getLockerResult();
        assertNotNull(lockerResult);
        assertTrue(lockerResult.isSuccess());
        assertNull(lockerResult.getErrorMessage());
        assertNotNull(lockerResult.getTicket());
        assertEquals(firstLocker, result.getLocker());
    }

    private void initRobotWithAllEmptyLockers() {
        System.out.println(1);
        firstLocker = new Locker(1);
        middleLocker = new Locker(0);
        lastLocker = new Locker(1);
        List<Locker> lockers = new ArrayList<>();
        lockers.add(firstLocker);
        lockers.add(middleLocker);
        lockers.add(lastLocker);
        robot = new LockerRobot(lockers);
    }

}
