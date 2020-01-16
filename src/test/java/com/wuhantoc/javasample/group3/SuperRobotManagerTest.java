package com.wuhantoc.javasample.group3;


import org.junit.jupiter.api.Test;

public class SuperRobotManagerTest {


    //机器人经理主动去存包1任意存，成功
    @Test
    void should_get_ticket_and_store_in_locker_when_supermanager_robot_store_cargo_given_supermanager_robot_and_available_locker(){
        //given
        //when
        //then

    }

    //机器人经理主动去存包2按顺序存，成功
    void should_get_ticket_and_store_in_locker1_when_supermanager_robot_store_cargo_given_supermanager_robot_and_empty_locker1_and_full_locker2_and_empty_locker3(){

    }
    //机器人经理主动去存包3包含空置率的存，成功
    void should_get_ticket_and_store_in_locker1_when_supermanager_robot_store_cargo_given_supermanager_robot_and_100_percent_empty_locker1_and_50_percent_empty_locker2(){

    }
    //机器人经理主动去存包，失败
    void should_get_error_message_when_supermanager_robot_store_cargo_given_supermanager_and_full_locker(){

    }
    //机器人经理命令robot去存包，成功
    void should_get_ticket_and_store_in_locker_when_supermanager_robot_store_cargo_given_supermanager_robot_and_robot_and_avaliable_locker(){

    }
    //机器人经理命令robot去存包2，成功
    void should_get_ticket_and_store_in_locker1_when_supermanager_store_cargo_given_supermanager_robot_and_robot_and_empty_locker1_and_full_locker2_and_empty_locker3(){

    }
    //机器人经理命令robot去存包，失败
    void should_get_error_message_when_supermanager_robot_store_cargo_given_ssupermanager_robot_and_robot_and_full_locker(){

    }
    //机器人经理命令superrobot去存包，成功
    void should_get_ticket_and_store_in_locker1_when_supermanager_robot_store_cargo_given_supermanager_robot_and_super_robot_and_100_percent_empty_locker1_and_50_percent_empty_locker2(){

    }
    //机器人经理命令superrobot去存包，失败
    void should_get_error_message_when_supermanager_robot_store_cargo_given_supermanager_robot_and_super_robot_and_full_locker(){

    }
    //机器人经理使用正确的票主动去取包
    void should_take_out_cargo_when_supermanager_robot_take_out_cargo_given_correct_ticket_and_supermanager_robot(){

    }
    //机器人经理使用用过的票主动去取包
    void should_get_error_message_when_supermanager_robot_take_out_cargo_given_used_ticket_and_supermanager_robot(){

    }
    //机器人经理使用错误的票主动去取包
    void should_get_error_message_when_supermanager_robot_take_out_cargo_given_error_ticket_and_supermanager_robot(){

    }
    //机器人经理命令robot去取包，使用正确的票
    void should_take_out_cargo_when_supermanager_robot_take_out_cargo_given_supermanager_robot_and_robot_and_correct_ticket(){

    }
    //机器人经理命令robot去取包，使用用过的票
    void should_get_error_message_when_supermanager_robot_take_out_cargo_given_supermanager_robot_and_robot_and_used_ticket(){

    }
    //机器人经理命令robot去取包，使用错误的票
    void should_get_error_message_when_supermanager_robot_take_out_cargo_given_supermanager_robot_and_robot_and_error_ticket(){

    }
    //机器人经理命令superrobot去取包，使用正确的票
    void should_take_out_cargo_when_supermanager_robot_take_out_cargo_given_supermanager_robot_and_super_robot_and_correct_ticket(){

    }
    //机器人经理命令superrobot去取包，使用用过的票
    void should_get_error_message_when_supermanager_robot_take_out_cargo_given_supermanager_robot_and_super_robot_and_used_ticket(){

    }
    //机器人经理命令superrobot去取包，使用错误的票
    void should_get_error_message_when_supermanager_robot_take_out_cargo_given_supermanager_robot_and_super_robot_and_error_ticket(){

    }


}
