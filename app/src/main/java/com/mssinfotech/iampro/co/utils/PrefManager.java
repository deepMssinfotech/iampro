package com.mssinfotech.iampro.co.utils;

/**
 * Created by mssinfotech on 6/24/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.text.StaticLayout;
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

    public static void updateUserData(final Context context, String uid){
        String myid = null;
        if(uid == null){
            String id= getLoginDetail(context,"id");
            if(!id.isEmpty()){
                myid = id;
            }
        }else{
            myid = uid;
        }
        if(myid != null) {
            String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + myid + "&uid=" + myid;
            Log.d(Config.TAG, myurl);
            StringRequest stringRequest = new StringRequest(myurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject result = null;
                            try {
                                Log.d(Config.TAG, response);
                                result = new JSONObject(response);
                                //Storing the Array of JSON String to our JSON Array
                                SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(IS_LOGIN, true);
                                editor.putString("username",result.getString("username"));
                                editor.putString("img_url",result.getString("avatar"));
                                editor.putString("id",result.getString("id"));
                                editor.putString("mobile",result.getString("mobile"));
                                editor.putString("fname",result.getString("fname"));
                                editor.putString("lname",result.getString("lname"));
                                editor.putString("email",result.getString("email"));
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
    public void saveLoginDetails(String username, String password,String imgurl,String id,String mobile,String fname,String lname,String email,String banner_image, String img_banner_image, String video_banner_image, String profile_image_gallery, String profile_video_gallery) {
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

        editor.putString("img_banner_image",img_banner_image);
        editor.putString("video_banner_image",video_banner_image);
        editor.putString("profile_image_gallery",profile_image_gallery);
        editor.putString("profile_video_gallery",profile_video_gallery);

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
    public static void logout(Context context){
        SharedPreferences preferences = context.getSharedPreferences("LoginDetails", MODE_PRIVATE);
        preferences.edit().remove("username").commit();
        preferences.edit().remove("img_url").commit();
        preferences.edit().remove("id").commit();
        preferences.edit().remove("mobile").commit();
        preferences.edit().remove("fname").commit();
        preferences.edit().remove("lname").commit();
        preferences.edit().remove("email").commit();
        preferences.edit().remove("banner_image").commit();
        preferences.edit().remove("img_banner_image").commit();
        preferences.edit().remove("video_banner_image").commit();
        preferences.edit().remove("profile_image_gallery").commit();
        preferences.edit().remove("profile_video_gallery").commit();
    }

}
