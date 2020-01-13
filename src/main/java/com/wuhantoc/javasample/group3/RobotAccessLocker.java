package com.wuhantoc.javasample.group3;

public interface RobotAccessLocker {

    RobotStoreResult robotStore();

    RobotTakeOutResult robotTakeOut(String ticket);

}
