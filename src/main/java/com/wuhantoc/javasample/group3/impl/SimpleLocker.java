package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.RobotStoreResult;
import com.wuhantoc.javasample.group3.RobotTakeOutResult;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserStoreResult;
import com.wuhantoc.javasample.group3.UserSuperRobotAccessLocker;
import com.wuhantoc.javasample.group3.UserTakeOutResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

public class SimpleLocker implements UserSuperRobotAccessLocker {

    private final UserRobotAccessLockerBox[] boxes;
    private final Map<String, UserRobotAccessLockerBox> ticketBoxMap;

    private SimpleLocker(int capacity, Supplier<? extends UserRobotAccessLockerBox> lockerBoxSupplier) {
        boxes = new UserRobotAccessLockerBox[capacity];
        initBoxesWith(lockerBoxSupplier);
        ticketBoxMap = new HashMap<>(capacity);
    }

    public static UserSuperRobotAccessLocker initLocker(int capacity, Supplier<? extends UserRobotAccessLockerBox> lockerBoxSupplier) {
        return new SimpleLocker(capacity, lockerBoxSupplier);
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
        if (boxes.length == 0) {
            return 0;
        }
        return (boxes.length - ticketBoxMap.size()) / (double) boxes.length;
    }

    private void initBoxesWith(Supplier<? extends UserRobotAccessLockerBox> lockerBoxSupplier) {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = lockerBoxSupplier.get();
        }
    }

    private InnerStoreResult store() {
        if (isFull()) {
            return null;
        }
        String ticket = generateNonConflictingTicket();
        UserRobotAccessLockerBox box = findAnyUnusedBox();
        ticketBoxMap.put(ticket, box);
        return new InnerStoreResult(ticket, box);
    }

    private UserRobotAccessLockerBox takeOut(String ticket) {
        if (!ticketBoxMap.containsKey(ticket)) {
            return null;
        }
        return Objects.requireNonNull(ticketBoxMap.remove(ticket));
    }

    private boolean isFull() {
        return ticketBoxMap.size() == boxes.length;
    }

    private UserRobotAccessLockerBox findAnyUnusedBox() {
        return Stream.of(boxes)
                .filter(lockerBox -> !ticketBoxMap.containsValue(lockerBox))
                .findAny()
                .orElseThrow(ConcurrentModificationException::new);
    }

    private String generateNonConflictingTicket() {
        String ticket;
        do {
            ticket = generateTicket();
        } while (ticketBoxMap.containsKey(ticket));
        return ticket;
    }

    private String generateTicket() {
        return UUID.randomUUID().toString();
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class InnerStoreResult {
        String ticket;
        UserRobotAccessLockerBox lockerBox;
    }

}
