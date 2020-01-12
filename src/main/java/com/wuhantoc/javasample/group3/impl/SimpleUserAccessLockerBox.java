package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;

public class SimpleUserAccessLockerBox implements UserRobotAccessLockerBox {

    @Override
    public void store(Cargo cargo) {

    }

    @Override
    public Cargo get() {
        return null;
    }
}
