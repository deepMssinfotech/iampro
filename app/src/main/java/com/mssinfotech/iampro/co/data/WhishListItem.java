package com.mssinfotech.iampro.co.data;
import java.io.Serializable;
public class WhishListItem {
    private int id;
    private String name, status, image, type_image;
    private String price;
    private static final long serialVersionUID = 1L;
    public WhishListItem() {
    }

    public WhishListItem(int id, String name, String image, String status, String price, String type_image) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.price = price;
        this.type_image = type_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType_image() {
        return type_image;
    }

    public void setType_image(String type_image) {
        this.type_image = type_image;
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


}