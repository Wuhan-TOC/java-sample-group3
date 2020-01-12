package com.wuhantoc.javasample.group3;

public interface RobotAccessLockerBox extends LockerBox {

    void store(Cargo cargo);

    Cargo get();

}
