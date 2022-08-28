package com.yxz.amadeus.ems.Table.bean;

public class DeviceBean {
    String deviceID, deviceName, model, manufactor, recordDate, userID, userName;
    int maintenanceTimes;

    public DeviceBean() {
    }

    public DeviceBean(String deviceID, String deviceName, String model, String manufactor, String recordDate, String userID, String userName,
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

}
