package com.mssinfotech.iampro.co.model;

/**
 * Created by dell on 27/07/2017.
 */
import java.io.Serializable;

public class Message implements Serializable {
    String id, message, createdAt;
    User user;

    public Message() {
    }

    public Message(String id, String message, String createdAt, User user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

