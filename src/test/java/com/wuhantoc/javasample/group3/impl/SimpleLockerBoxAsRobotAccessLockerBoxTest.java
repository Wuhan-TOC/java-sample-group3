package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.RobotAccessLockerBox;
import com.wuhantoc.javasample.group3.exception.EmptyLockerBoxException;
import com.wuhantoc.javasample.group3.exception.NotEmptyLockerBoxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleLockerBoxAsRobotAccessLockerBoxTest {

    private static final Cargo anyCargo = new Cargo() {
    };

    @Test
    void should_not_throw_any_exception_when_store_given_an_empty_closed_cell() {
        //given
        RobotAccessLockerBox lockerBox = initEmptyRobotAccessBox();
        //when
        lockerBox.store(anyCargo);
        //then
        //no exception is thrown
    }

    @Test
    void should_throw_not_empty_locker_box_exception_when_store_given_not_empty_box() {
        //given
        RobotAccessLockerBox lockerBox = initNotEmptyRobotAccessBox();
        //when & then
        assertThrows(NotEmptyLockerBoxException.class, () -> lockerBox.store(anyCargo));
    }

    @Test
    void should_get_the_cargo_when_get_given_locker_box_stored_the_cargo() {
        //given
        RobotAccessLockerBox lockerBox = initEmptyRobotAccessBox();
        lockerBox.store(anyCargo);
        //when
        Cargo result = lockerBox.get();
        //then
        assertEquals(anyCargo, result);
    }

    @Test
    void should_throw_empty_locker_box_exception_when_get_given_an_empty_box() {
        //given
        RobotAccessLockerBox lockerBox = initEmptyRobotAccessBox();
        //when & then
        assertThrows(EmptyLockerBoxException.class, lockerBox::get);
    }

    private RobotAccessLockerBox initEmptyRobotAccessBox() {
        return SimpleLockerBox.supplier().get();
    }

    private RobotAccessLockerBox initNotEmptyRobotAccessBox() {
        SimpleLockerBox lockerBox = SimpleLockerBox.supplier().get();
        lockerBox.store(anyCargo);
        return lockerBox;
    }

}
