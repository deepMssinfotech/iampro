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
    private static final String IS_LOGIN = "IsLoggedIn";
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "mss-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        userpref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        usereditor = userpref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
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
                                usereditor.putString("username", result.getString("username"));
                                usereditor.putString("img_url", result.getString("avatar"));
                                usereditor.putString("id", result.getString("id"));
                                usereditor.putString("dob", result.getString("dob"));
                                usereditor.putString("mobile", result.getString("mobile"));
                                usereditor.putString("fname", result.getString("fname"));
                                usereditor.putString("lname", result.getString("lname"));
                                usereditor.putString("email", result.getString("email"));
                                usereditor.putString("banner_image", result.getString("banner_image"));
                                usereditor.putString("img_banner_image", result.getString("img_banner_image"));
                                usereditor.putString("video_banner_image", result.getString("video_banner_image"));
                                usereditor.putString("profile_image_gallery", result.getString("profile_image_gallery"));
                                usereditor.putString("profile_video_gallery", result.getString("profile_video_gallery"));
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

    public static boolean isLogin(Context context){
        if (userpref==null || userpref.getString("id", null)==null){
             return false;
        }
        String value= userpref.getString("id", null);
        if(value==null){
            return false;
        }else{
            return true;
        }
    }
    public static boolean updateLoginDetail(Context context, String field, String value) {
        usereditor.remove(field);
        usereditor.putString(field, value);
        usereditor.apply();
        return true;
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
