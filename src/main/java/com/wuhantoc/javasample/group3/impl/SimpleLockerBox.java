package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.RobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.exception.EmptyLockerBoxException;
import com.wuhantoc.javasample.group3.exception.NotEmptyLockerBoxException;

public class SimpleLockerBox implements UserAccessLockerBox, RobotAccessLockerBox {

    private static final Cargo CARGO_STORED_BY_USER = new Cargo() {
    };

    private Cargo cargo;

    @Override
    public void store(Cargo cargo) {
        ensureEmpty();
        this.cargo = cargo;
    }

    @Override
    public Cargo get() {
        ensureNotEmpty();
        return removeCargo();
    }

    private void ensureEmpty() {
        if (cargo != null) {
            throw new NotEmptyLockerBoxException();
        }
    }

    private void ensureNotEmpty() {
        if (cargo == null) {
            throw new EmptyLockerBoxException();
        }
    }

    private Cargo removeCargo() {
        Cargo ret = cargo;
        cargo = null;
        return ret;
    }

}
