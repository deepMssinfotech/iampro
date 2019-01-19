package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 16/01/19.
 */

public class DataModel {
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

}
