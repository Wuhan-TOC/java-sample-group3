package com.wuhantoc.javasample.group3.locker;

import com.wuhantoc.javasample.group3.lockerbox.LockerBox;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.locker.LockerStoreResult.storeLockerFail;
import static com.wuhantoc.javasample.group3.locker.LockerStoreResult.storeLockerSuccess;
import static com.wuhantoc.javasample.group3.locker.LockerTakeOutResult.takeOutLockerFail;
import static com.wuhantoc.javasample.group3.locker.LockerTakeOutResult.takeOutLockerSuccess;

public class Locker {

    private final LockerBox[] boxes;
    private final Map<String, LockerBox> ticketBoxMap;

    private Locker(int capacity, Supplier<LockerBox> lockerBoxSupplier) {
        boxes = new LockerBox[capacity];
        initBoxesWith(lockerBoxSupplier);
        ticketBoxMap = new HashMap<>(capacity);
    }

    public static Locker initLocker(int capacity, Supplier<LockerBox> lockerBoxSupplier) {
        return new Locker(capacity, lockerBoxSupplier);
    }

    public LockerStoreResult store() {
        if (isFull()) {
            return storeLockerFail(USER_STORE_FAIL_MESSAGE);
        }
        String ticket = generateNonConflictingTicket();
        LockerBox box = findAnyUnusedBox();
        ticketBoxMap.put(ticket, box);
        return storeLockerSuccess(ticket, box);
    }

    public LockerTakeOutResult takeOut(String ticket) {
        if (!ticketBoxMap.containsKey(ticket)) {
            return takeOutLockerFail(USER_TAKE_OUT_FAIL_MESSAGE);
        }
        return takeOutLockerSuccess(ticketBoxMap.remove(ticket));
    }

    public double getVacancyRate() {
        if (boxes.length == 0) {
            return 0;
        }
        return (double) (boxes.length - ticketBoxMap.size()) / boxes.length;
    }

    private void initBoxesWith(Supplier<LockerBox> lockerBoxSupplier) {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = lockerBoxSupplier.get();
        }
    }

    private boolean isFull() {
        return ticketBoxMap.size() == boxes.length;
    }

    private LockerBox findAnyUnusedBox() {
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
