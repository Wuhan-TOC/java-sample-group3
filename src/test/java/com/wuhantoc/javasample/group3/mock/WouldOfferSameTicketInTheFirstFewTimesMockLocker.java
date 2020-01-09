package com.wuhantoc.javasample.group3.mock;

import com.wuhantoc.javasample.group3.GetPackageResult;
import com.wuhantoc.javasample.group3.Locker;
import com.wuhantoc.javasample.group3.LockerSavePackageResult;

import static com.wuhantoc.javasample.group3.GetPackageResult.getPackageSuccess;

public class WouldOfferSameTicketInTheFirstFewTimesMockLocker implements Locker {

    public static final String THE_FIRST_TICKET = "the first ticket";

    public static final String THE_SECOND_TICKET = "the second ticket";

    private final int secondTicketThreshold;

    private int saveCount = 0;

    public WouldOfferSameTicketInTheFirstFewTimesMockLocker(int secondTicketThreshold) {
        if (secondTicketThreshold <= 0) {
            throw new IllegalArgumentException("secondTicketThreshold must be positive");
        }
        this.secondTicketThreshold = secondTicketThreshold;
    }

    @Override
    public LockerSavePackageResult savePackage() {
        saveCount++;
        if (saveCount < secondTicketThreshold) {
            return LockerSavePackageResult.savePackageSuccess(THE_FIRST_TICKET);
        }
        return LockerSavePackageResult.savePackageSuccess(THE_SECOND_TICKET);
    }

    @Override
    public GetPackageResult getPackage(String ticket) {
        return getPackageSuccess();
    }
}
