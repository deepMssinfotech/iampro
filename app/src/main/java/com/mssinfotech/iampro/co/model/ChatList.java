package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 23/02/19.
 */

public class ChatList {
    String from_id;
    String to_id;

    public ChatList(String from_id, String to_id, String avatar, String messageType, String msg, String udate) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.avatar = avatar;
        this.messageType = messageType;
        this.msg = msg;
        this.udate = udate;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    String avatar;
    String messageType;
    String msg;
    String udate;
}
