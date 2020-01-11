package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.RobotStoreResult;
import com.wuhantoc.javasample.group3.RobotTakeOutResult;
import com.wuhantoc.javasample.group3.UserRobotAccessLockerBox;
import com.wuhantoc.javasample.group3.UserStoreResult;
import com.wuhantoc.javasample.group3.UserSuperRobotAccessLocker;
import com.wuhantoc.javasample.group3.UserTakeOutResult;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.UserStoreResult.storeFail;
import static com.wuhantoc.javasample.group3.UserStoreResult.storeSuccess;

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
    public UserStoreResult store() {
        if (isFull()) {
            return storeFail(USER_STORE_FAIL_MESSAGE);
        }
        String ticket = generateNonConflictingTicket();
        UserRobotAccessLockerBox box = findAnyUnusedBox();
        ticketBoxMap.put(ticket, box);
        return storeSuccess(ticket, box);
    }

    @Override
    public UserTakeOutResult takeOut(String ticket) {
        if (!ticketBoxMap.containsKey(ticket)) {
            return UserTakeOutResult.takeOutFail(USER_TAKE_OUT_FAIL_MESSAGE);
        }
        UserRobotAccessLockerBox lockerBox = ticketBoxMap.remove(ticket);
        Objects.requireNonNull(lockerBox).get();
        return UserTakeOutResult.takeOutSuccess(lockerBox);
    }

    @Override
    public RobotStoreResult storeCargo() {
        if (isFull()) {
            return RobotStoreResult.storeFail(USER_STORE_FAIL_MESSAGE);
        }
        String ticket = generateNonConflictingTicket();
        UserRobotAccessLockerBox box = findAnyUnusedBox();
        ticketBoxMap.put(ticket, box);
        return RobotStoreResult.storeSuccess(ticket, box);
    }

    @Override
    public RobotTakeOutResult takeOutCargo(String ticket) {
        if (!ticketBoxMap.containsKey(ticket)) {
            return RobotTakeOutResult.takeOutFail(USER_TAKE_OUT_FAIL_MESSAGE);
        }
        return RobotTakeOutResult.takeOutSuccess(Objects.requireNonNull(ticketBoxMap.remove(ticket)));
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
}
