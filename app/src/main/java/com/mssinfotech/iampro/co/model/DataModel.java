package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 16/01/19.
 */

public class DataModel {
    public String text;
    public int drawable;
    public String color;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String pid;
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    private float rating;
    private int uid,isliked;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsliked() {
        return isliked;
    }

    public void setIsliked(int isliked) {
        this.isliked = isliked;
    }

    private String type;

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

    private String fullname;
    private String userImage;

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

    private int totallike;
     private int comments;
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
     private int sCost;

    public String getDaysago() {
        return daysago;
    }

    public void setDaysago(String daysago) {
        this.daysago = daysago;
    }

    private String daysago;
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

    private int pCost;

    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    private String is_favourite;
    public DataModel(String t, int d, String c)
    {
        text=t;
        drawable=d;
        color=c;
    }
    public DataModel(String name,String image,String udate,String category){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
    }
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.isliked=isliked;
    }
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments,int sCost,int pCost){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.sCost=sCost;
        this.pCost=pCost;
        this.isliked = isliked;
    }
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments,String daysago){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
         this.daysago=daysago;
         this.isliked = isliked;
    }
    //product provide demand
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments,int sCost,int pCost,float rating,int uid,String fullname,String userImage,String pid,String type){
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
        this.isliked = isliked;
        this.type= type;
    }
    //product provide demand
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments,int sCost,int pCost,float rating,int uid,String fullname,String userImage,String pid,String type,String is_favourite){
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
        this.isliked = isliked;
        this.type= type;
        this.is_favourite=is_favourite;
    }
    //name,image,udate,categoryv,totallike,comments,daysago,ratingv,uid,fullname,avatar
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments,String daysago,float rating,int uid,String fullname,String userImage){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
         this.daysago=daysago;
        this.rating=rating;
        this.uid=uid;
        this.fullname=fullname;
        this.userImage=userImage;
        this.isliked =isliked;
    }
    //image
    public DataModel(String name,String image,String udate,String category,int totallike,int isliked,int comments,String daysago,float rating,int uid,String fullname,String userImage,int id,String type){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.daysago=daysago;
        this.rating=rating;
        this.uid=uid;
        this.fullname=fullname;
        this.userImage=userImage;
        this.id=id;
        this.type=type;
        this.isliked = isliked;
    }
    public DataModel(String name,String image,String udate,String category,int totallike,int comments,int sCost,int pCost,String daysago){
        this.name=name;
        this.image=image;
        this.udate=udate;
        this.category=category;
        this.totallike=totallike;
        this.comments=comments;
        this.sCost=sCost;
        this.pCost=pCost;
        this.daysago=daysago;
        this.isliked = isliked;
    }

}
