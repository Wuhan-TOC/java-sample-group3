package com.wuhantoc.javasample.group3;

import lombok.Data;

@Data
public class Locker {

    private boolean FullLocker;

    void fillToTheFull() {
        this.FullLocker=true;
    }

    public SavePackageResult savePackage() {
        SavePackageResult savePackageResult=new SavePackageResult();
        if(this.FullLocker==true){
            savePackageResult.setTicket(null);
            savePackageResult.setSuccesssFlag(false);
            savePackageResult.setErrorMessage("Locker is Full");
        }else{
            savePackageResult.setTicket("ticket");
            savePackageResult.setSuccesssFlag(true);
            savePackageResult.setErrorMessage(null);
        }
        return  savePackageResult;
    }

    public GetPackageResult getPackage(String ticket) {
        GetPackageResult getPackageResult=new GetPackageResult();
        if(ticket.equals("ticket")){
            getPackageResult.setSuccessFlag(true);
            getPackageResult.setErrorMessage(null);
        }else{
            getPackageResult.setSuccessFlag(false);
            getPackageResult.setErrorMessage("you ticket is not correct");
        }
        return getPackageResult;
    }


}
