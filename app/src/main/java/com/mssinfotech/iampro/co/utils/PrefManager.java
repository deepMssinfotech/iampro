package com.mssinfotech.iampro.co.utils;

/**
 * Created by mssinfotech on 6/24/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.common.Config;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static void updateUserData(Context context,String uid){
        String myid = null;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", MODE_PRIVATE);
        if(uid == null){
            String id= sharedPreferences.getString("id","");
            if(!id.isEmpty()){
                myid = id;
            }
        }else{
            myid = uid;
        }
        if(myid != null) {
            String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + uid + "&uid=" + uid;
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            StringRequest stringRequest = new StringRequest(myurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject result = null;
                            //Log.d(Config.TAG, response);
                            try {
                                result = new JSONObject(response);
                                //Storing the Array of JSON String to our JSON Array
                                editor.putBoolean(IS_LOGIN, true);
                                editor.putString("username",result.getString("username"));
                                editor.putString("password", result.getString("password"));
                                editor.putString("img_url",result.getString("username"));
                                editor.putString("id",result.getString("username"));
                                editor.putString("mobile",result.getString("username"));
                                editor.putString("fname",result.getString("username"));
                                editor.putString("lname",result.getString("username"));
                                editor.putString("email",result.getString("username"));
                                editor.putString("banner_image",result.getString("banner_image"));
                                editor.putString("img_banner_image",result.getString("img_banner_image"));
                                editor.putString("video_banner_image",result.getString("video_banner_image"));
                                editor.putString("profile_image_gallery",result.getString("profile_image_gallery"));
                                editor.putString("profile_video_gallery",result.getString("profile_video_gallery"));
                                editor.commit();
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
