package com.yxz.amadeus.ems.Table.bean;

public class RepairInfoBean {
    String infoID, deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, userID, username,principal,principalName;
    float cost;

    public RepairInfoBean() {
    }

    public RepairInfoBean(String infoID, String deviceID,String deviceName, String damageDate, String damageDegree, String damageCause, String repairDate,
                      String repairPersonnel, String state, String userID, String username, float cost,String principal,String principalName) {
        this.infoID = infoID;
        this.deviceID = deviceID;
        this.deviceName=deviceName;
        this.damageDate = damageDate;
        this.damageDegree = damageDegree;
        this.damageCause = damageCause;
        this.repairDate = repairDate;
        this.repairPersonnel = repairPersonnel;
        this.state = state;
        this.userID = userID;
        this.username = username;
        this.cost = cost;
        this.principal=principal;
        this.principalName=principalName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
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

    public String getDamageDate() {
        return damageDate;
    }

    public void setDamageDate(String damageDate) {
        this.damageDate = damageDate;
    }

    public String getDamageDegree() {
        return damageDegree;
    }

    public void setDamageDegree(String damageDegree) {
        this.damageDegree = damageDegree;
    }

    public String getDamageCause() {
        return damageCause;
    }

    public void setDamageCause(String damageCause) {
        this.damageCause = damageCause;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    public String getRepairPersonnel() {
        return repairPersonnel;
    }

    public void setRepairPersonnel(String repairPersonnel) {
        this.repairPersonnel = repairPersonnel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

}
