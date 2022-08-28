package com.yxz.amadeus.ems.Table.bean;

public class UseInfoBean {
    String infoID, deviceID,deviceName, useDate, userID, userName, statusBefore, statusAfter, remarks,adopt,principal,principalName;

    public UseInfoBean() {
    }

    public UseInfoBean(String infoID, String deviceID,String deviceName, String useDate, String userID, String userName,String statusBefore, String statusAfter, String remarks,String adopt,String principal,String principalName) {
        this.infoID = infoID;
        this.deviceID = deviceID;
        this.deviceName=deviceName;
        this.useDate = useDate;
        this.userID = userID;
        this.statusBefore = statusBefore;
        this.statusAfter = statusAfter;
        this.remarks = remarks;
        this.userName = userName;
        this.adopt=adopt;
        this.principal=principal;
        this.principalName=principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipal() {
        return principal;
    }
    public String getAdopt(){return adopt;}

    public void setAdopt(String adopt) {
        this.adopt = adopt;
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

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
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

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getStatusBefore() {
        return statusBefore;
    }

    public void setStatusBefore(String statusBefore) {
        this.statusBefore = statusBefore;
    }

    public String getStatusAfter() {
        return statusAfter;
    }

    public void setStatusAfter(String statusAfter) {
        this.statusAfter = statusAfter;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
