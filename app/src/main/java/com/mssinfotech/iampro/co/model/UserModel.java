package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 18/01/19.
 */

public class UserModel {
    public String text;
    public int drawable;
    public String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String url;
    private String description;

    private String name;

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    private String profession;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    private String udate;
    private String image;
    private String category;
    private String total_image;
    private String total_video;
    private String total_friend;

    public String getTotal_image() {
        return total_image;
    }

    public void setTotal_image(String total_image) {
        this.total_image = total_image;
    }

    public String getTotal_video() {
        return total_video;
    }

    public void setTotal_video(String total_video) {
        this.total_video = total_video;
    }

    public String getTotal_friend() {
        return total_friend;
    }

    public void setTotal_friend(String total_friend) {
        this.total_friend = total_friend;
    }

    public String getTotal_product() {
        return total_product;
    }

    public void setTotal_product(String total_product) {
        this.total_product = total_product;
    }

    public String getTotal_provide() {
        return total_provide;
    }

    public void setTotal_provide(String total_provide) {
        this.total_provide = total_provide;
    }

    public String getTotal_demand() {
        return total_demand;
    }

    public void setTotal_demand(String total_demand) {
        this.total_demand = total_demand;
    }

    private String total_product;
    private String total_provide;
    private String total_demand;
    private String is_friend;
    private String friend_status;

    public String getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    public String getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(String friend_status) {
        this.friend_status = friend_status;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getIs_block() {
        return is_block;
    }

    public void setIs_block(int is_block) {
        this.is_block = is_block;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    private String tid;
      private int is_block;
       private String user_url;
    private String uid;
    public UserModel(String t, int d, String c)
    {
        text=t;
        drawable=d;
        color=c;
    }
    public UserModel(int id,String name,String image,String udate,String category){
        this.id=id;
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
    }
    public UserModel(int id,String name,String image,String udate,String category,String total_image,String total_video,String total_friend,String total_product,String total_provide,String total_demand,String is_friend,String friend_status,String tid,int is_block,String user_url){
        this.id=id;
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.total_image=total_image;
        this.total_video=total_video;
        this.total_friend=total_friend;
        this.total_product=total_product;
        this.total_provide=total_provide;
        this.total_demand=total_demand;
        //is_friend,friend_status,tid,is_block,user_url
        this.is_friend=is_friend;
        this.friend_status=friend_status;
        this.tid=tid;
        this.is_block=is_block;
        this.user_url=user_url;
    }
    public UserModel(int id,String name,String image,String udate,String category,String total_image,String total_video,String total_friend,String total_product,String total_provide,String total_demand){
        this.id=id;
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
         this.total_image=total_image;
         this.total_video=total_video;
        this.total_friend=total_friend;
         this.total_product=total_product;
         this.total_provide=total_provide;
         this.total_demand=total_demand;
    }
}
