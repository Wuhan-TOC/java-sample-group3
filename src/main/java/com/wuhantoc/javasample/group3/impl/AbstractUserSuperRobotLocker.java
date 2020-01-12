package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.RobotStoreResult;
import com.wuhantoc.javasample.group3.RobotTakeOutResult;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserStoreResult;
import com.wuhantoc.javasample.group3.UserSuperRobotAccessLocker;
import com.wuhantoc.javasample.group3.UserTakeOutResult;

import java.util.function.Supplier;

import static com.wuhantoc.javasample.group3.RobotStoreResult.robotStoreFail;
import static com.wuhantoc.javasample.group3.RobotStoreResult.robotStoreSuccess;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.robotTakeOutFail;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.robotTakeOutSuccess;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.UserStoreResult.userStoreFail;
import static com.wuhantoc.javasample.group3.UserStoreResult.userStoreSuccess;
import static com.wuhantoc.javasample.group3.UserTakeOutResult.userTakeOutFail;
import static com.wuhantoc.javasample.group3.UserTakeOutResult.userTakeOutSuccess;

abstract class AbstractUserSuperRobotLocker extends AbstractLocker<UserRobotAccessLockerBox> implements UserSuperRobotAccessLocker {

    protected AbstractUserSuperRobotLocker(int capacity, Supplier<UserRobotAccessLockerBox> lockerBoxSupplier) {
        super(capacity, lockerBoxSupplier, UserRobotAccessLockerBox.class);
    }

    @Override
    public UserStoreResult userStore() {
        InnerStoreResult storeResult = store();
        if (storeResult == null) {
            return userStoreFail(USER_STORE_FAIL_MESSAGE);
        }
        return userStoreSuccess(storeResult.ticket, storeResult.lockerBox);
    }

    @Override
    public UserTakeOutResult userTakeOut(String ticket) {
        UserRobotAccessLockerBox lockerBox = takeOut(ticket);
        if (lockerBox == null) {
            return userTakeOutFail(USER_TAKE_OUT_FAIL_MESSAGE);
        }
        lockerBox.get();
        return userTakeOutSuccess(lockerBox);
    }

    @Override
    public RobotStoreResult robotStore() {
        InnerStoreResult storeResult = store();
        if (storeResult == null) {
            return robotStoreFail(ROBOT_STORE_FAIL_MESSAGE);
        }
        return robotStoreSuccess(storeResult.ticket, storeResult.lockerBox);
    }

    @Override
    public RobotTakeOutResult robotTakeOut(String ticket) {
        UserRobotAccessLockerBox lockerBox = takeOut(ticket);
        if (lockerBox == null) {
            return robotTakeOutFail(ROBOT_TAKE_OUT_FAIL_MESSAGE);
        }
        return robotTakeOutSuccess(lockerBox);
    }

    @Override
    public double getEmptyRate() {
        if (getCapacity() == 0) {
            return 0;
        }
        return (getCapacity() - getSize()) / (double) getCapacity();
    }

}
