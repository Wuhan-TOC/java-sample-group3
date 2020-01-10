package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.Cargo;
import com.wuhantoc.javasample.group3.RobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.exception.EmptyLockerBoxException;
import com.wuhantoc.javasample.group3.exception.NotEmptyLockerBoxException;

import java.util.function.Supplier;

public class SimpleLockerBox implements UserAccessLockerBox, RobotAccessLockerBox {

    private Cargo cargo;

    private SimpleLockerBox() {
        super();
    }

    public static Supplier<SimpleLockerBox> supplier() {
        return SimpleLockerBox::new;
    }

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
