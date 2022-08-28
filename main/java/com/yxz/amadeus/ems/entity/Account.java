package com.yxz.amadeus.ems.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Account extends HashMap<String, Account> implements Parcelable{

    String type;
    String userID;
    String password;
    String username;
    String grade;
    String dept;
    String telephone;

    public Account(){}
    public Account(String type, String userID, String password, String username, String grade, String dept, String telephone){
        this.type=type;
        this.userID=userID;
        this.password=password;
        this.username=username;
        this.grade=grade;
        this.dept=dept;
        this.telephone=telephone;
        }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    protected Account(Parcel in) {

    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
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
