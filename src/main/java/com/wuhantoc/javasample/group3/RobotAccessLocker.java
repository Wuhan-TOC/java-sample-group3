package com.wuhantoc.javasample.group3;

public interface RobotAccessLocker  {

    RobotStoreResult storeCargo();

    RobotTakeOutResult takeOutCargo(String ticket);

}
