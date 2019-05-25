package com.mssinfotech.iampro.co.models;

/**
 * Created by dell on 16/08/2017.
 */


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Kartik Sharma
 * Created on: 9/1/2016 , 8:35 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class User {
    public String uid;
    public String email;
    public String firebaseToken;

    private String lastMessage;
    private int notifCount;
    public User() {
    }

    public User(String uid, String email, String firebaseToken) {
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getNotifCount() {
        return notifCount;
    }

    public void setNotifCount(int notifCount) {
        this.notifCount = notifCount;
    }
}

