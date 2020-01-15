package com.wuhantoc.javasample.group3.lockerbox;

import com.wuhantoc.javasample.group3.Cargo;

public class LockerBox {

    private Cargo cargo;

    public void store(Cargo cargo) {
        this.cargo = cargo;
    }

    public Cargo get() {
        return cargo;
    }
}
