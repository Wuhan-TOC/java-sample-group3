package com.wuhantoc.javasample.group3;

public interface Locker {

    LockerSavePackageResult savePackage();

    GetPackageResult getPackage(String ticket);

}
