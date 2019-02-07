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
    private ArrayList<MyImageModel> allItemsInSection;


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

    public ArrayList<MyImageModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<MyImageModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
