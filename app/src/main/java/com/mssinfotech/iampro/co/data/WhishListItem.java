package com.mssinfotech.iampro.co.data;
import java.io.Serializable;
public class WhishListItem {
    private int id;
    private String name, status, image, timeStamp;
    private Float price;
    private static final long serialVersionUID = 1L;
    public WhishListItem() {
    }

    public WhishListItem(int id, String name, String image, String status, String timeStamp, Float price) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}