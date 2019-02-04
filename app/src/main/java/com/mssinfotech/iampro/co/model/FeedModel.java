package com.mssinfotech.iampro.co.model;

import java.util.ArrayList;

public class FeedModel {

    private int id;
    private String shareId;
    private String fullname;
    private int uid;
    private String avatar_path;
    private String udate;
    private long timespam;
    private String is_block;
    private ArrayList<String> imageArray;
    private String fimage_path;
    private int comment;

    public String getDetail_name() {
        return detail_name;
    }

    public void setDetail_name(String detail_name) {
        this.detail_name = detail_name;
    }

    public int getSelling_cost() {
        return selling_cost;
    }

    public void setSelling_cost(int selling_cost) {
        this.selling_cost = selling_cost;
    }

    public int getPurchese_cost() {
        return purchese_cost;
    }

    public void setPurchese_cost(int purchese_cost) {
        this.purchese_cost = purchese_cost;
    }

    //detail_name,selling_cost,purchese_cost
    private String detail_name;
    private int selling_cost;
    private int purchese_cost;

    public FeedModel(int id, String shareId, String fullname,int uid, String avatar_path, String udate, long timespam, String is_block, ArrayList<String> imageArray, String fimage_path, int comment, int likes, int mylikes, int all_rating, String type, int all_comment, int average_rating,String detail_name,int selling_cost,int purchese_cost) {
        this.id = id;
        this.shareId = shareId;
        this.fullname = fullname;
        this.uid = uid;
        this.avatar_path = avatar_path;
        this.udate = udate;
        this.timespam = timespam;
        this.is_block = is_block;
        this.imageArray = imageArray;
        this.fimage_path = fimage_path;
        this.comment = comment;
        this.likes = likes;
        this.mylikes = mylikes;
        this.all_rating = all_rating;
        this.type = type;
        this.all_comment = all_comment;
        this.average_rating = average_rating;
        this.detail_name=detail_name;
        this.selling_cost=selling_cost;
         this.purchese_cost=purchese_cost;
    }

    public FeedModel(int id, String shareId, String fullname,int uid, String avatar_path, String udate, long timespam, String is_block, ArrayList<String> imageArray, String fimage_path, int comment, int likes, int mylikes, int all_rating, String type, int all_comment, int average_rating) {
        this.id = id;
        this.shareId = shareId;
        this.fullname = fullname;
        this.uid = uid;
        this.avatar_path = avatar_path;
        this.udate = udate;
        this.timespam = timespam;
        this.is_block = is_block;
        this.imageArray = imageArray;
        this.fimage_path = fimage_path;
        this.comment = comment;
        this.likes = likes;
        this.mylikes = mylikes;
        this.all_rating = all_rating;
        this.type = type;
        this.all_comment = all_comment;
        this.average_rating = average_rating;
    }

    private int likes;
    private int mylikes;
    private int all_rating;
    private String type;
    private int all_comment;
    private int average_rating;


    //id,shareid,fullname,uid,avatar_path,udate,timespam,is_block,imageArray,fimage_path,comment,likes,mylikes,all_rating,type,all_comment,average_rating
    private String avatar;

    private String image;

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getFimage_path() {
        return fimage_path;
    }

    public void setFimage_path(String fimage_path) {
        this.fimage_path = fimage_path;
    }

    public int getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(int average_rating) {
        this.average_rating = average_rating;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getMylikes() {
        return mylikes;
    }

    public void setMylikes(int mylikes) {
        this.mylikes = mylikes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAll_comment() {
        return all_comment;
    }

    public void setAll_comment(int all_comment) {
        this.all_comment = all_comment;
    }

    public long getTimespam() {
        return timespam;
    }

    public void setTimespam(long timespam) {
        this.timespam = timespam;
    }

    public String getIs_block() {
        return is_block;
    }

    public void setIs_block(String is_block) {
        this.is_block = is_block;
    }

    public ArrayList<String> getImageArray() {
        return imageArray;
    }

    public void setImageArray(ArrayList<String> imageArray) {
        this.imageArray = imageArray;
    }

    public int getAll_rating() {
        return all_rating;
    }

    public void setAll_rating(int all_rating) {
        this.all_rating = all_rating;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public FeedModel(String image) {
        this.image = image;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String fimage_path) {
        this.fimage_path = fimage_path;
    }

}
