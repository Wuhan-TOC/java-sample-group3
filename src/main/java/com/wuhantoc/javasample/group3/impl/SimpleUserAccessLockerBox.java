package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;

public class SimpleUserAccessLockerBox implements UserRobotAccessLockerBox {

    private Cargo cargo;

    @Override
    public void store(Cargo cargo) {
        this.cargo = cargo;
    }

    @Override
    public Cargo get() {
        return cargo;
    }
}
