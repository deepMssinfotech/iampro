package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 25/01/19.
 */

public class Review {
    private String fname;
    private String email;
    private String comments;
    private String id;
    private String pcid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String pid;

    public Review(String fname, String email, String comments, String id, String pcid, String user_image, String rdate, String added_by,String pid) {
        this.fname = fname;
        this.email = email;
        this.comments = comments;
        this.id = id;
        this.pcid = pcid;
        this.user_image = user_image;
        this.rdate = rdate;
        this.added_by = added_by;
        this.pid=pid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPcid() {
        return pcid;
    }

    public void setPcid(String pcid) {
        this.pcid = pcid;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    private String user_image;
    private String rdate;
    private String added_by;

}
