package com.mssinfotech.iampro.co.model;

/**
 * Created by mssinfotech on 15/01/19.
 */

import java.util.ArrayList;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 */
public class SectionImageModel {

    private String headerTitle;
    private String albemId;
    private ArrayList<MyImageModel> allItemsInSection;
    private String more;
    public SectionImageModel() {

    }
    public SectionImageModel(String headerTitle, ArrayList<MyImageModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public void setAlbemId(String albemId) {
        this.albemId=albemId;
    }
    public String getAlbemId() {
        return albemId;
    }
    public ArrayList<MyImageModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<MyImageModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public void setMore(String more) {
        this.more =more;
    }
    public String getMore() {
        return more;
    }
}
