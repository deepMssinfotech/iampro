package com.mssinfotech.iampro.co.data;

public class MyProductItem {

    public String fullname;
    public String avatar;
    public Integer id;
    public String email;
    public String msg;
    public String unread;
    public String is_block;
    public void setid(Integer id) {
        this.id = id;
    }

    public Integer getid() {
        return id;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getfullname() {
        return fullname;
    }

    public void setavatar(String avatar) {
        this.avatar = avatar;
    }

    public String getavatar() {
        return avatar;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getemail() {
        return email;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public String getmsg() {
        return msg;
    }

    public void setunread(String unread) {
        this.unread = unread;
    }

    public String getunread() {
        return unread;
    }

    public void setis_block(String is_block) {
        this.is_block = is_block;
    }

    public String getis_block() {
        return is_block;
    }
}