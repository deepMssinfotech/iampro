package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 02/02/19.
 */

public class MyProductModel {
    private String name;
    private String image;
    private String udate;
    private String category;
    private int totallike;
    private int comments;
    private int sCost;
    private String more;
    public String getMore() {
        return more;
    }
    public void setMore(String more) {
        this.more = more;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotallike() {
        return totallike;
    }

    public void setTotallike(int totallike) {
        this.totallike = totallike;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getsCost() {
        return sCost;
    }

    public void setsCost(int sCost) {
        this.sCost = sCost;
    }

    public int getpCost() {
        return pCost;
    }

    public void setpCost(int pCost) {
        this.pCost = pCost;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    int pCost;
    float rating;
    int uid;
    String fullname;
    String userImage;
    String pid;
    public MyProductModel(String name,String image,String udate,String category,int totallike,int comments,int sCost,int pCost,float rating,int uid,String fullname,String userImage,String pid){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.sCost=sCost;
        this.pCost=pCost;
        this.rating=rating;
        this.uid=uid;
        this.fullname=fullname;
        this.userImage=userImage;
        this.uid=uid;
        this.pid=pid;
    }
    //load more
    public MyProductModel(String name,String image,String udate,String category,int totallike,int comments,int sCost,int pCost,float rating,int uid,String fullname,String userImage,String pid,String more){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.sCost=sCost;
        this.pCost=pCost;
        this.rating=rating;
        this.uid=uid;
        this.fullname=fullname;
        this.userImage=userImage;
        this.uid=uid;
        this.pid=pid;
        this.more=more;
    }
    //pdemand
    public MyProductModel(String name,String image,String udate,String category,int totallike,int comments,int sCost,float rating,int uid,String fullname,String userImage,String pid){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.sCost=sCost;
        this.rating=rating;
        this.uid=uid;
        this.fullname=fullname;
        this.userImage=userImage;
        this.uid=uid;
        this.pid=pid;
    }
     //load more
    public MyProductModel(String name,String image,String udate,String category,int totallike,int comments,int sCost,float rating,int uid,String fullname,String userImage,String pid,String more){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.sCost=sCost;
        this.rating=rating;
        this.uid=uid;
        this.fullname=fullname;
        this.userImage=userImage;
        this.uid=uid;
        this.pid=pid;
        this.more=more;
    }
}
