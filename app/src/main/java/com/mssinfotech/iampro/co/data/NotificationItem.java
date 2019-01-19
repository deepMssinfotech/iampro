package com.mssinfotech.iampro.co.data;

public class NotificationItem {
    private int id;
    private String product_name, product_image, user_name, user_image,udate,notify_type,product_type,detail;
    private static final long serialVersionUID = 1L;
    public NotificationItem() {
    }

    public NotificationItem(int id, String product_name, String product_image,String user_name, String user_image, String udate, String notify_type, String product_type,String detail) {
        super();
        this.id = id;
        this.detail = detail;
        this.product_image = product_image;
        this.product_name = product_name;
        this.user_name = user_name;
        this.user_image = user_image;
        this.udate = udate;
        this.notify_type = notify_type;
        this.product_type = product_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }
    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}