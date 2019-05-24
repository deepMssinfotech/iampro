package com.mssinfotech.iampro.co.data;

/**
 * Created by mssinfotech on 16/03/19.
 */

public class ImageModel {
    //id ,heading,slider_type,link,image,slider_image,status,language,lorder,no,index,mindex
     private String id;
    private String heading;
    private String slider_type;
    private String link;
    private String image;
    private String slider_image;
    private String status;
    private String language;
    private String lorder;
    private int no;
    private int index;
    private String mindex;
    public ImageModel(String id, String heading, String slider_type, String link, String image, String slider_image, String status, String language, String lorder, int no, int index, String mindex) {
        this.id = id;
        this.heading = heading;
        this.slider_type = slider_type;
        this.link = link;
        this.image = image;
        this.slider_image = slider_image;
        this.status = status;
        this.language = language;
        this.lorder = lorder;
        this.no = no;
        this.index = index;
        this.mindex = mindex;
    }



    //id,heading,slider_type,link,imagev,slider_image,status,language,lorder,no,index,mindex
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSlider_type() {
        return slider_type;
    }

    public void setSlider_type(String slider_type) {
        this.slider_type = slider_type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public void setSlider_image(String slider_image) {
        this.slider_image = slider_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLorder() {
        return lorder;
    }

    public void setLorder(String lorder) {
        this.lorder = lorder;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMindex() {
        return mindex;
    }

    public void setMindex(String mindex) {
        this.mindex = mindex;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}