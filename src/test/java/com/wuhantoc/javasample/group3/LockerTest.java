package com.wuhantoc.javasample.group3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LockerTest {

    //存包正确
    @Test
    void should_generate_a_ticket_when_save_package_given_available_locker() {
        // given
        Locker locker = initAvailableLocker();
        // when
        String ticket = locker.savePackage();
        // then
        Assertions.assertNotNull(ticket);
    }

    private Locker initAvailableLocker() {
        return new Locker();
    }

    @Test
    void should_generate_a_ticket_when_get_package_given_correct_ticket() {
        // given
        Locker locker = initAvailableLocker();
        String ticket = getCorrectTicket(locker);
        // when
        boolean result = locker.getPackage(ticket);
        // then
        Assertions.assertTrue(result);
    }

    private String getCorrectTicket(Locker availableLocker) {
        return availableLocker.savePackage();
    }


}
