package com.mssinfotech.iampro.co.data;
public class JoinFriendItem {
    private String country;
    private int total_product;
    private String city;
    private int total_friend;
    private int total_product_demand;
    private int total_img;
    private int user_id;
    private int friend_id;
    private int total_video;
    private int total_product_provide;
    private int id;
    private String state;
    private String username;
    private String avatar;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    private String tid;
    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    private String user_url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //image
    private String image;

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    public String getTotal_image() {
        return total_image;
    }

    public void setTotal_image(String total_image) {
        this.total_image = total_image;
    }

    ///total_image
    private String total_image;
    private String udate;
    public JoinFriendItem(String avatar, String fullname, String category) {
        this.avatar = avatar;
        this.fullname = fullname;
        this.category = category;
    }
    //total_provide,total_demand
    private String total_provide;

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

    private String total_demand;
    public JoinFriendItem(int id,String name,String image,String udate,String category,String total_image,String total_video,String total_friend,String total_product,String total_provide,String total_demand,String is_friend,String friend_status,String tid,int is_block,String user_url,String city){
        this.id=id;
        this.fullname=name;
        this.city = city;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.total_image=total_image;
        this.total_video=Integer.parseInt(total_video);
        this.total_friend=Integer.parseInt(total_friend);
        this.total_product=Integer.parseInt(total_product);
        this.total_provide=total_provide;
        this.total_demand=total_demand;

        this.is_friend=is_friend;
        this.friend_status=friend_status;
        this.tid=tid;
        this.is_block=is_block;
        this.user_url=user_url;
    }
    private String fullname;
    private String category;

    private int is_block;

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

    private String is_friend;
    private String friend_status;

    public int getIs_block() {
        return is_block;
    }

    public void setIs_block(int is_block) {
        this.is_block = is_block;
    }

    public JoinFriendItem() {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotal_product() {
        return total_product;
    }

    public void setTotal_product(int total_product) {
        this.total_product = total_product;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTotal_friend() {
        return total_friend;
    }

    public void setTotal_friend(int total_friend) {
        this.total_friend = total_friend;
    }

    public int getTotal_product_demand() {
        return total_product_demand;
    }

    public void setTotal_product_demand(int total_product_demand) {
        this.total_product_demand = total_product_demand;
    }

    public int getTotal_img() {
        return total_img;
    }

    public void setTotal_img(int total_img) {
        this.total_img = total_img;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getTotal_video() {
        return total_video;
    }

    public void setTotal_video(int total_video) {
        this.total_video = total_video;
    }

    public int getTotal_product_provide() {
        return total_product_provide;
    }

    public void setTotal_product_provide(int total_product_provide) {
        this.total_product_provide = total_product_provide;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}