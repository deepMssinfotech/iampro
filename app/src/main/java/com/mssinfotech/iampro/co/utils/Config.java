package com.mssinfotech.iampro.co.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Config
{

    public static final String TAG="Imapro tag";
    public static boolean doubleBackToExitPressedOnce = false;
    public static final String API_URL="https://www.iampro.co/api/";
    public static final String URL_ROOT="https://www.iampro.co/";
    public static final String AJAX_URL="https://www.iampro.co/ajax/";
    public static final String AVATAR_URL="https://www.iampro.co/uploads/avatar/";
    public static final String BANNER_URL="https://www.iampro.co/uploads/media/";

    public static final String IMAGE_DIRECTORY = "/iampro";
    public static final Integer GALLERY = 1, CAMERA = 2, PICK_IMAGE_MULTIPLE = 3;
    public static String layoutName="";
    public Config(){

    }
    public static void setLayoutName(String name){
        layoutName=name;
    }
    public static String getLayoutName(){
        return layoutName;
    }
    /**  Network connectivity  */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi=true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile=true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public static void showInternetDialog(Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Oops you are offline!")
                .setMessage("Check your network connectivity and try again...")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                }).show();
    }

    public static void sendRequestToServer() {
         Log.d(TAG,"test servide for 5 sec");
    }

}
