package com.wuhantoc.javasample.group3;

public interface UserAccessLocker {

    UserStoreResult store();

    UserTakeOutResult takeOut(String ticket);

}
