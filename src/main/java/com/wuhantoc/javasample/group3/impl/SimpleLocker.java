package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.UserAccessLocker;
import com.wuhantoc.javasample.group3.UserAccessLockerBox;
import com.wuhantoc.javasample.group3.UserStoreResult;
import com.wuhantoc.javasample.group3.UserTakeOutResult;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.wuhantoc.javasample.group3.TextConstant.FULL_LOCKER_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USE_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.UserStoreResult.storeFail;
import static com.wuhantoc.javasample.group3.UserStoreResult.storeSuccess;

public class SimpleLocker implements UserAccessLocker {

    private final UserAccessLockerBox[] boxes;
    private final Map<String, UserAccessLockerBox> ticketBoxMap;

    private SimpleLocker(int capacity, Supplier<? extends UserAccessLockerBox> lockerBoxSupplier) {
        boxes = new SimpleLockerBox[capacity];
        initBoxesWith(lockerBoxSupplier);
        ticketBoxMap = new HashMap<>(capacity);
    }

    public static UserAccessLocker initLocker(int capacity, Supplier<? extends UserAccessLockerBox> lockerBoxSupplier) {
        return new SimpleLocker(capacity, lockerBoxSupplier);
    }

    @Override
    public UserStoreResult store() {
        if (isFull()) {
            return storeFail(FULL_LOCKER_MESSAGE);
        }
        String ticket = generateNonConflictingTicket();
        UserAccessLockerBox box = findAnyUnusedBox();
        ticketBoxMap.put(ticket, box);
        return storeSuccess(ticket, box);
    }

    @Override
    public UserTakeOutResult takeOut(String ticket) {
        if (!ticketBoxMap.containsKey(ticket)) {
            return UserTakeOutResult.takeOutFail(USE_TAKE_OUT_FAIL_MESSAGE);
        }
        return UserTakeOutResult.takeOutSuccess(Objects.requireNonNull(ticketBoxMap.remove(ticket)));
    }

    private void initBoxesWith(Supplier<? extends UserAccessLockerBox> lockerBoxSupplier) {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = lockerBoxSupplier.get();
        }
    }

    private boolean isFull() {
        return ticketBoxMap.size() == boxes.length;
    }

    private UserAccessLockerBox findAnyUnusedBox() {
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
