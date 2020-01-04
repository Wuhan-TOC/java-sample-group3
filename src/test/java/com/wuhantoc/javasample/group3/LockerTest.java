package com.wuhantoc.javasample.group3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LockerTest {

    private Locker initAvailableLocker() {
        return new Locker();
    }

    //存包正确
    @Test
    void should_generate_a_ticket_when_save_package_given_available_locker() {
        // given
        Locker locker = initAvailableLocker();
        // when
        SavePackageResult ticket = locker.savePackage();
        // then
        Assertions.assertNotNull(ticket);
        Assertions.assertTrue(ticket.isSuccesssFlag());
        Assertions.assertNull(ticket.getErrorMessage());
        Assertions.assertNotNull(ticket.getTicket());
    }

    private Locker initFullLocker() {
        Locker locker = new Locker();
        locker.fillToTheFull();
        return locker;
    }

    //存包错误
    @Test
    void should_get_error_message_when_save_package_given_full_locker() {
        // given
        Locker locker = initFullLocker();
        // when
        SavePackageResult errorMessage = locker.savePackage();
        // then
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNotNull(errorMessage.getErrorMessage());
        Assertions.assertFalse(errorMessage.isSuccesssFlag());
        Assertions.assertNull(errorMessage.getTicket());
    }

    private String initCorrectTicket(Locker availableLocker){
        SavePackageResult result = availableLocker.savePackage();
        if(result.isSuccesssFlag()){
            return result.getTicket();
        }
        throw new RuntimeException(result.getErrorMessage());
    }

    //正确取包
    @Test
    void should_get_package_when_get_package_given_correct_ticket() {

        //given
        Locker locker = initAvailableLocker();
        String correctTicket = initCorrectTicket(locker);
        //when
        GetPackageResult getAPackage = locker.getPackage(correctTicket);
        //then
        Assertions.assertNotNull(getAPackage);
        Assertions.assertTrue(getAPackage.isSuccessFlag());
        Assertions.assertNull(getAPackage.getErrorMessage());
    }

    private String initWrongTicket(Locker availableLocker){
        SavePackageResult result = availableLocker.savePackage();
        if(result.isSuccesssFlag()){
            return result.getTicket()+"#wrong";
        }
        throw new RuntimeException(result.getErrorMessage());
    }

    //错误取包
    @Test
    void should_get_error_message_when_get_message_given_error_ticket() {

        //given
        Locker locker = initAvailableLocker();
        String wrongTicket = initWrongTicket(locker);
        //when
        GetPackageResult getAPackage = locker.getPackage(wrongTicket);
        //then
        Assertions.assertNotNull(getAPackage);
        Assertions.assertFalse(getAPackage.isSuccessFlag());
        Assertions.assertNotNull(getAPackage.getErrorMessage());
    }


}
