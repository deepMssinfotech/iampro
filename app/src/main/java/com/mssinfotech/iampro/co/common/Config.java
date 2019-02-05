package com.mssinfotech.iampro.co.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.utils.PrefManager;

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

    public static final String OTHER_IMAGE_URL="https://www.iampro.co/uploads/product/";
    public static final String V_URL="https://www.iampro.co/uploads/v_image/";
    public static final String BANNER_URL="https://www.iampro.co/uploads/media/";
    public static final String MEDIA_URL="https://www.iampro.co/uploads/media/";
    public static final String ALBUM_URL="https://www.iampro.co/uploads/album/w/500/";
    public static final String IP_ADDRESS="";
    public static TextView count_chat = null, count_notify = null, count_cart=null, count_whishlist = null, count_friend_request = null;
    public static final String IMAGE_DIRECTORY = "/iampro/image";
    public static final String VIDEO_DIRECTORY = "/iampro/video";
    public static final Integer GALLERY = 1, CAMERA = 2, PICK_IMAGE_MULTIPLE = 3;
    public static String layoutName="";
    public static String ResponceResult="";
    public static String PAGE_TAG="page_tag";
    public static String PREVIOUS_PAGE_TAG = null;

    public static RequestOptions options_avatar = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.iampro)
            .error(R.drawable.iampro)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_background = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_product = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_image = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_video= new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_provide = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_demand = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static int STORAGE_PERMISSION_CODE = 123;
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

    public static void sendRequestToServer(Context context) {
        if (PrefManager.isLogin(context)) {
            //Log.d(TAG, "test servide for 5 sec");
            String api_url = Config.API_URL + "chat.php?type=chat_count&myid=" + PrefManager.getLoginDetail(context, "id");
            //Log.d(Config.TAG, api_url);
             {
                StringRequest stringRequest = new StringRequest(api_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject result = null;
                                //Log.d(Config.TAG, response);
                                try {
                                    result = new JSONObject(response);
                                    //Storing the Array of JSON String to our JSON Array
                                    if (count_chat != null) count_chat.setText(result.getString("chatcount"));
                                    if(count_notify != null) count_notify.setText(result.getString("my_notification"));
                                    if(count_cart != null) count_cart.setText(result.getString("cart_count"));
                                    if(count_whishlist != null) count_whishlist.setText(result.getString("my_wishlist"));
                                    if(count_friend_request != null) count_friend_request.setText(result.getString("panding_friend"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(Config.TAG, error.toString());
                            }
                        });

                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        }
    }
}
