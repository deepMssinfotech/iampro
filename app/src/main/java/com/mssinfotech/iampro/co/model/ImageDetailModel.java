package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 30/01/19.
 */

public class ImageDetailModel {


    private int id;
    private String name;
    private String image;
    private String about_us;
    private String like_unlike;
    private int rating;
    private int comments;
    private int totallike;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String fullname;
      private String avatar;
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    private int uid;

    public String getUdatee() {
        return udatee;
    }

    public void setUdatee(String udatee) {
        this.udatee = udatee;
    }

    private String udatee;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    public ImageDetailModel(int id,String name, String image, String about_us, String like_unlike, int rating, int comments, int totallike,int uid,String fullname,String avatar,String udatee,String category,String type) {
        this.id=id;
        this.name = name;
        this.image = image;
        this.about_us = about_us;
        this.like_unlike = like_unlike;
        this.rating = rating;
        this.comments = comments;
        this.totallike = totallike;
        this.uid=uid;
         this.fullname=fullname;
          this.avatar=avatar;
           this.udatee=udatee;
            this.category=category;
             this.type=type;
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

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }

    public String getLike_unlike() {
        return like_unlike;
    }

    public void setLike_unlike(String like_unlike) {
        this.like_unlike = like_unlike;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getTotallike() {
        return totallike;
    }

    public void setTotallike(int totallike) {
        this.totallike = totallike;
    }
}
