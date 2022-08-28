package com.yxz.amadeus.ems.Table.bean;

public class CheckInfoBean {
    String infoID, deviceID,deviceName, lastCheck,
            nextCheck, lastRepair, state, inspector,inspectorName, userID,userName;//inspector检验人/userID负责人
    int cycle;

    public CheckInfoBean(){}
    public CheckInfoBean(String infoID,String deviceID,String deviceName,String lastCheck,
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

}
