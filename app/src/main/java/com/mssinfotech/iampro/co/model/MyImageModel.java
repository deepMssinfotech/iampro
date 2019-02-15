package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 04/02/19.
 */

public class MyImageModel {
    //id,added_by,name,category,albem_type,image,udate,about_us,group_id,is_featured,status,is_block,comments,totallike,like_unlike,rating,uid
    private String id;
    private String albemid;
    private String name;
    private String category;
    private String albem_type;
    private String image;
    private String udate;
    private String about_us;
    private String group_id;
    private String is_featured;
    private String status;
    private String is_block;
    private String comments;
    private String totallike;
    private String like_unlike;
    private String rating;
    private String uid;

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    private String more;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbemid() {
        return albemid;
    }

    public void setAlbemid(String albemid) {
        this.albemid = albemid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAlbem_type() {
        return albem_type;
    }

    public void setAlbem_type(String albem_type) {
        this.albem_type = albem_type;
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

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_block() {
        return is_block;
    }

    public void setIs_block(String is_block) {
        this.is_block = is_block;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTotallike() {
        return totallike;
    }

    public void setTotallike(String totallike) {
        this.totallike = totallike;
    }

    public String getLike_unlike() {
        return like_unlike;
    }

    public void setLike_unlike(String like_unlike) {
        this.like_unlike = like_unlike;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public MyImageModel(String id, String albemid, String name, String category, String albem_type, String image, String udate, String about_us, String group_id, String is_featured, String status, String is_block, String comments, String totallike, String like_unlike, String rating, String uid) {
        this.id = id;
        this.albemid = albemid;
        this.name = name;
        this.category = category;
        this.albem_type = albem_type;
        this.image = image;
        this.udate = udate;
        this.about_us = about_us;
        this.group_id = group_id;
        this.is_featured = is_featured;
        this.status = status;
        this.is_block = is_block;
        this.comments = comments;
        this.totallike = totallike;
        this.like_unlike = like_unlike;
        this.rating = rating;
        this.uid = uid;
    }
    //fragment load more
    public MyImageModel(String id, String albemid, String name, String category, String albem_type, String image, String udate, String about_us, String group_id, String is_featured, String status, String is_block, String comments, String totallike, String like_unlike, String rating, String uid,String more) {
        this.id = id;
        this.albemid = albemid;
        this.name = name;
        this.category = category;
        this.albem_type = albem_type;
        this.image = image;
        this.udate = udate;
        this.about_us = about_us;
        this.group_id = group_id;
        this.is_featured = is_featured;
        this.status = status;
        this.is_block = is_block;
        this.comments = comments;
        this.totallike = totallike;
        this.like_unlike = like_unlike;
        this.rating = rating;
        this.uid = uid;
        this.more=more;
    }
}
