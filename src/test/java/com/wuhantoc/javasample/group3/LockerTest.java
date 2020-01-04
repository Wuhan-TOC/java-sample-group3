package com.wuhantoc.javasample.group3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LockerTest {

    private Locker initAvailableLocker() { return new Locker(); }
    private Locker initFullLocker() {

        Locker locker=new Locker();
        locker.setFullLocker(true);
        return locker;

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



    @Test
    //存包错误
    void should_get_error_message_when_save_package_given_full_locker() {
        // given
        Locker locker=initFullLocker();
        // when
        SavePackageResult errorMessage =locker.savePackage();
        // then
        Assertions.assertNotNull(errorMessage);
        Assertions.assertNotNull(errorMessage.getErrorMessage());
        Assertions.assertFalse(errorMessage.isSuccesssFlag());
        Assertions.assertNull(errorMessage.getTicket());
    }



   @Test
    //正确取包
    void should_get_package_when_get_package_given_correct_ticket(){

        //given
       Locker locker=initAvailableLocker();
       String correctTicket="Correct_Ticket";
       //when
       GetPackageResult getAPackage=locker.getPackage(correctTicket);
       //then
       Assertions.assertNotNull(getAPackage);
       Assertions.assertTrue(getAPackage.isSuccessFlag());
       Assertions.assertNotNull(getAPackage.getApackage());
       Assertions.assertNull(getAPackage.getErrorMessage());

   }

   @Test
    //错误取包
    void should_get_error_message_when_get_message_given_error_ticket(){

        //given
        Locker locker=initAvailableLocker();
        String errorTicket="Error_Ticket";
        //when
        GetPackageResult getAPackage=locker.getPackage(errorTicket);
        //then
        Assertions.assertNotNull(getAPackage);
        Assertions.assertFalse(getAPackage.isSuccessFlag());
        Assertions.assertNotNull(getAPackage.getErrorMessage());
        Assertions.assertNull(getAPackage.getApackage());
    }


}
