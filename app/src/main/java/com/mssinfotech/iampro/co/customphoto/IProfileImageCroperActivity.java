package com.mssinfotech.iampro.co.customphoto;

public interface IProfileImageCroperActivity {
    void initViews();

    void findViews();

    void makeLayoutSquare();


    void hideCropping();

    void showCropping();

    void onGetImages(String action);

    void createTempFile();

    void takePic();

    void pickImage();

    void initClickListner();
}