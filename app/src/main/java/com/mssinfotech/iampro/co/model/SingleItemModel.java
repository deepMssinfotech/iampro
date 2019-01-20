package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 15/01/19.
 */

public class SingleItemModel {


    private String name;
    private String url;
    private String description;

    private String udate;
    private String image;
    private String category;
    private int totallike;
    private int uid;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String fullname;
     private String avatar;

      //,daysago,totallike,comments
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
    public String getDaysago() {
        return daysago;
    }
    public void setDaysago(String daysago) {
        this.daysago = daysago;
    }
    private int comments;
     private String daysago;

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public SingleItemModel() {
    }

    public SingleItemModel(String name, String url) {
        this.name = name;
        this.url = url;
    }
    public SingleItemModel(String name, String image,String udate) {
        this.name = name;
        this.image = image;
        this.udate=udate;
    }
    //daysago,totallike,comments   uid,fullname,avatar
   public SingleItemModel(String name, String image,String udate,String daysago,int totallike,int comments,int uid,String fullname,String avatar) {
        this.name = name;
        this.image = image;
        this.udate=udate;
       this.daysago=daysago;
        this.totallike=totallike;
        this.comments=comments;
        this.uid=uid;
         this.fullname=fullname;
          this.avatar=avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
