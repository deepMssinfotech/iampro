package com.mssinfotech.iampro.co.utils;

/**
 * Created by mssinfotech on 6/24/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static final String IS_LOGIN = "IsLoggedIn";
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void saveLoginDetails(String username, String password,String imgurl,String id,String mobile,String fname,String lname,String email,String banner_image) {
        //id,mobile,name,email
        SharedPreferences sharedPreferences = _context.getSharedPreferences("LoginDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString("username",username);
        editor.putString("password", password);
        editor.putString("img_url",imgurl);
        editor.putString("id",id);
        editor.putString("mobile",mobile);
        editor.putString("fname",fname);
        editor.putString("lname",lname);
        editor.putString("email",email);
        editor.putString("banner_image",banner_image);
        editor.commit();
    }
    public static boolean isLogin(Context context){
        SharedPreferences prefrence = context.getSharedPreferences("LoginDetails",MODE_PRIVATE);
        String id= prefrence.getString("id","");
        if(!id.isEmpty())return true;
        return false;
    }
    public static String getLoginDetail(Context context,String field){
        SharedPreferences prefrence = context.getSharedPreferences("LoginDetails",MODE_PRIVATE);
        return prefrence.getString(field,"");
    }

}
