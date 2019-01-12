package com.mssinfotech.iampro.co.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Config
{

    public static final String TAG="Imapro tag";

    public static final String API_URL="http://www.iampro.co/api/";
    public static final String AJAX_URL="http://www.iampro.co/ajax/";
    public static final String AVATAR_URL="http://www.iampro.co/uploads/avatar/";

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
                        // Whatever...
                        //Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        //startActivity(intent);
                        //finish();
                    }
                }).show();

    }


}
