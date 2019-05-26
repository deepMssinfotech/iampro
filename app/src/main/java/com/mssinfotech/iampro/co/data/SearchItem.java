package com.mssinfotech.iampro.co.data;

public class SearchItem {
    private int id;
    private String avg_rating,comments,totallike,name, selling_cost, image, purchese_cost,user_id,user_fullname,user_avatar,type,category,city,total_product,total_provide,total_demend,total_friends,total_image,total_video;
    private String price;
    private static final long serialVersionUID = 1L;
    public SearchItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_Id(String user_Id) {
        this.user_id = user_id;
    }
    public String getUser_Id() {
        return user_id;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }
    public String getAvg_rating() {
        return avg_rating;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }
    public void setTotallike(String totallike) {
        this.totallike = totallike;
    }
    public String getTotallike() {
        return totallike;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setTotal_product(String total_product) {
        this.total_product = total_product;
    }
    public String getTotal_product() {
        return total_product;
    }
    public void setTotal_provide(String total_provide) {
        this.total_provide = total_provide;
    }
    public String getTotal_provide() {
        return total_provide;
    }
    public void setTotal_demend(String total_demend) {
        this.total_demend = total_demend;
    }
    public String getTotal_demend() {
        return total_demend;
    }
    public void setTotal_friends(String total_friends) {
        this.total_friends = total_friends;
    }
    public String getTotal_friends() {
        return total_friends;
    }
    public void setTotal_image(String total_image) {
        this.total_image = total_image;
    }
    public String getTotal_image() {
        return total_image;
    }
    public void setTotal_video(String total_video) {
        this.total_video = total_video;
    }
    public String getTotal_video() {
        return total_video;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getSelling_cost() {
        return selling_cost;
    }

    public void setSelling_cost(String selling_cost) {
        this.selling_cost = selling_cost;
    }

    public String getPurchese_cost() {
        return purchese_cost;
    }

    public void setPurchese_cost(String purchese_cost) {
        this.purchese_cost = purchese_cost;
    }


}