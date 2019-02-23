package com.mssinfotech.iampro.co.models;

/**
 * Created by dell on 16/08/2017.
 */


/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 2:25 PM
 * Project: FirebaseChat
 */

public class Users {
    private String emailId;
    private String lastMessage;
    private int notifCount;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
