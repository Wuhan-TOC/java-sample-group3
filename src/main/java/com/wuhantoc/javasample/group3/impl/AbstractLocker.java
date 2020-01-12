package com.wuhantoc.javasample.group3.impl;

import com.wuhantoc.javasample.group3.LockerBox;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

abstract class AbstractLocker<L extends LockerBox> {

    private final L[] boxes;
    private final Map<String, L> ticketBoxMap;

    @SuppressWarnings("unchecked")
    protected AbstractLocker(int capacity, Supplier<L> lockerBoxSupplier, Class<L> lockerBoxClass) {
        boxes = (L[]) Array.newInstance(lockerBoxClass, capacity);
        initBoxesWith(lockerBoxSupplier);
        ticketBoxMap = new HashMap<>(capacity);
    }

    abstract String generateTicket();

    int getCapacity() {
        return boxes.length;
    }

    int getSize() {
        return ticketBoxMap.size();
    }

    InnerStoreResult store() {
        if (isFull()) {
            return null;
        }
        String ticket = generateNonConflictingTicket();
        L box = findAnyUnusedBox();
        ticketBoxMap.put(ticket, box);
        return new InnerStoreResult(ticket, box);
    }

    L takeOut(String ticket) {
        if (!ticketBoxMap.containsKey(ticket)) {
            return null;
        }
        return Objects.requireNonNull(ticketBoxMap.remove(ticket));
    }

    private void initBoxesWith(Supplier<L> lockerBoxSupplier) {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = lockerBoxSupplier.get();
        }
    }

    private boolean isFull() {
        return ticketBoxMap.size() == boxes.length;
    }

    private L findAnyUnusedBox() {
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

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class InnerStoreResult {
        String ticket;
        L lockerBox;
    }

}
