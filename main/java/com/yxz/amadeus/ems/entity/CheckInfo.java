package com.yxz.amadeus.ems.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class CheckInfo extends HashMap<String, Account> implements Parcelable {

    String infoID, deviceID,deviceName, lastCheck,
            nextCheck, lastRepair, state, inspector,inspectorName, userID,userName;
    int cycle;

    public CheckInfo(){}
    public CheckInfo(String infoID,String deviceID,String deviceName,String lastCheck,
                     String nextCheck,String lastRepair,String state,String inspector,String inspectorName,String userID,String userName,int cycle){
        this.infoID=infoID;
        this.deviceID=deviceID;
        this.deviceName=deviceName;
        this.lastCheck=lastCheck;
        this.nextCheck=nextCheck;
        this.lastRepair=lastRepair;
        this.state=state;
        this.inspector=inspector;
        this.inspectorName=inspectorName;
        this.userID=userID;
        this.userName=userName;
        this.cycle=cycle;
    }

    public String getDeviceName(){return deviceName;}

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getInfoID() {
        return infoID;
    }

    public void setInfoID(String infoID) {
        this.infoID = infoID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(String lastCheck) {
        this.lastCheck = lastCheck;
    }

    public String getNextCheck() {
        return nextCheck;
    }

    public void setNextCheck(String nextCheck) {
        this.nextCheck = nextCheck;
    }

    public String getLastRepair() {
        return lastRepair;
    }

    public void setLastRepair(String lastRepair) {
        this.lastRepair = lastRepair;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }
    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getUserName(){return userName;}

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    protected CheckInfo(Parcel in) {
    }

    public static final Creator<CheckInfo> CREATOR = new Creator<CheckInfo>() {
        @Override
        public CheckInfo createFromParcel(Parcel in) {
            return new CheckInfo(in);
        }

        @Override
        public CheckInfo[] newArray(int size) {
            return new CheckInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
