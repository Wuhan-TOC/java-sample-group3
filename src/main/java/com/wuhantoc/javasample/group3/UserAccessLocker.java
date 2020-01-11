package com.wuhantoc.javasample.group3;

public interface UserAccessLocker {

    UserStoreResult userStore();

    UserTakeOutResult userTakeOut(String ticket);

}
