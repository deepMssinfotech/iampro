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


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static SharedPreferences userpref = null;
    private static SharedPreferences.Editor usereditor = null;
    public static final String IS_LOGIN = "IsLoggedIn";
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "mss-welcome";
    private static final String PREF_USER_NAME = "mss-user";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        userpref = _context.getSharedPreferences(PREF_USER_NAME, PRIVATE_MODE);
        usereditor = userpref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public static void updateCountFromServer(final Context context,String uid) {
        if (!uid.isEmpty()) {
            //Log.d(TAG, "test servide for 5 sec");
            String api_url = Config.API_URL + "api.php?type=chat_count&myid=" + uid;
            StringRequest stringRequest = new StringRequest(api_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject result = null;
                            //Log.d(Config.TAG, response);
                            try {
                                result = new JSONObject(response);
                                String f_total_count_product = result.getString("total_count_product");
                                String f_total_count_provide = result.getString("total_count_provide");
                                String f_total_count_demand = result.getString("total_count_demand");
                                String f_total_count_image = result.getString("total_count_image");
                                String f_total_count_video = result.getString("total_count_video");
                                String f_total_count_friend = result.getString("total_count_friend");
                                saveCountDetails(f_total_count_product, f_total_count_provide, f_total_count_demand, f_total_count_image, f_total_count_friend, f_total_count_video);
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
    public static void updateUserData(final Context context, String myid){
        if(myid != null) {
            String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + myid + "&uid=" + myid;
            //Log.d(Config.TAG, myurl);
            StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    JSONObject result = null;
                    try {
                        //Log.d(Config.TAG, response);
                        result = new JSONObject(response);
                        String id = result.getString("id");
                        String unamee = result.getString("username");
                        String img_url= result.getString("avatar");
                        String dob = result.getString("dob");
                        String mobile = result.getString("mobile");
                        String fname = result.getString("fname");
                        String lname = result.getString("lname");
                        String email = result.getString("email");
                        String banner_image = result.getString("banner_image");
                        String img_banner_image = result.getString("img_banner_image");
                        String video_banner_image = result.getString("video_banner_image");
                        String profile_image_gallery = result.getString("profile_image_gallery");
                        String profile_video_gallery = result.getString("profile_video_gallery");
                        PrefManager.saveLoginDetails(unamee,img_url,id,mobile,fname,lname,email,dob,banner_image,  img_banner_image,  video_banner_image,  profile_image_gallery,  profile_video_gallery);
                        //saveLoginDetails();
                        usereditor.commit();
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
    public static void saveCountDetails(String total_count_product,String total_count_provide,String total_count_demand,String total_count_image,String total_count_friend,String total_count_video){
        usereditor.putString("total_count_product", total_count_product);
        usereditor.putString("total_count_provide", total_count_provide);
        usereditor.putString("total_count_demand", total_count_demand);
        usereditor.putString("total_count_image", total_count_image);
        usereditor.putString("total_count_friend", total_count_friend);
        usereditor.putString("total_count_video", total_count_video);
        usereditor.commit();
    }
    public static void saveLoginDetails(String unamee,String avatarv,String id,String mobilev,String fnamem,String lnamem,String email,String dob,String banner_imagev,  String img_banner_image,  String video_banner_image,  String profile_image_gallery,  String profile_video_gallery){
        usereditor.putString("username", unamee);
        usereditor.putString("img_url", avatarv);
        usereditor.putString("id", id);
        usereditor.putString("dob", dob);
        usereditor.putString("mobile", mobilev);
        usereditor.putString("fname", fnamem);
        usereditor.putString("lname", lnamem);
        usereditor.putString("email", email);
        usereditor.putString("banner_image", banner_imagev);
        usereditor.putString("img_banner_image", img_banner_image);
        usereditor.putString("video_banner_image", video_banner_image);
        usereditor.putString("profile_image_gallery", profile_image_gallery);
        usereditor.putString("profile_video_gallery", profile_video_gallery);
        usereditor.commit();
    }
    public static boolean isLogin(Context context){
        if(userpref==null)
            return false;
        else
            return userpref.getBoolean(IS_LOGIN,false);
    }
    public static boolean updateLoginDetail(Context context, String field, String value) {
        usereditor.remove(field);
        usereditor.putString(field, value);
        usereditor.apply();
        return true;
    }
    public static void setLogin(boolean isLogin) {
        usereditor.putBoolean(IS_LOGIN, isLogin);
        usereditor.commit();
    }
    public static String getLoginDetail(Context context, String field){
        String value= userpref.getString(field, null);
        usereditor.commit();
        return value;
    }
    public static void logout(Context context){
        usereditor.clear();
        usereditor.commit(); // commit changes
    }
}
