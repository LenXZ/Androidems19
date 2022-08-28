package com.yxz.amadeus.ems.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Device extends HashMap<String, Account> implements Parcelable {
    String deviceID, deviceName, model, manufactor, recordDate, userID, userName;
    int maintenanceTimes;

    public Device() {
    }

    public Device(String deviceID, String deviceName, String model, String manufactor, String recordDate, String userID, String userName,
                  int maintenanceTimes) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.model = model;
        this.manufactor = manufactor;
        this.recordDate = recordDate;
        this.userID = userID;
        this.userName = userName;
        this.maintenanceTimes = maintenanceTimes;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufactor() {
        return manufactor;
    }

    public void setManufactor(String manufactor) {
        this.manufactor = manufactor;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getMaintenanceTimes() {
        return maintenanceTimes;
    }

    public void setMaintenanceTimes(int maintenanceTimes) {
        this.maintenanceTimes = maintenanceTimes;
    }

    protected Device(Parcel in) {
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
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
