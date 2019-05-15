package com.mssinfotech.iampro.co.common;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.data.CategoryItem;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class function {

    public static void loadFragment(Context context, Fragment fragment, Bundle args) {
        // create a FragmentManager
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager(); //getFragmentManager();

        Fragment tmp = fm.findFragmentByTag(fragment.getClass().getName());
        if (tmp != null && tmp.isVisible()) {
            //Toast.makeText(context,"You are already in same page",Toast.LENGTH_LONG).show();
            //return;
        }
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        if(args != null){
            fragment.setArguments(args);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(android.R.id.content, fragment, fragment.getClass().getName());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

    public static void addtocart(Context context,String pid,String qty,String price){
        String url = Config.API_URL+ "cart.php?type=addtocart&p_type=product&pid="+pid+"&qty="+qty+"&price="+price+"&uid="+ PrefManager.getLoginDetail(context,"id") +"&ip_address="+ Config.IP_ADDRESS;
        Log.d(Config.TAG+"cart",url);
        function.executeUrl(context,"get", url, null);
    }
    public static String executeUrl(final Context context, String type, String url, final Map<String, String> params){
        //final ProgressDialog loading = ProgressDialog.show(context,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = null;
        //Log.d(Config.TAG,"url: "+url);
        if(type.equalsIgnoreCase("get")){
            stringRequest = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //loading.dismiss();
                            Config.ResponceResult = response;
                            //Log.d(Config.TAG,"result is : "+response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //loading.dismiss();
                    Config.ResponceResult = error.getMessage();
                    //Log.d(Config.TAG,"error : "+error.getMessage());
                }
            });
        }else if(type.equalsIgnoreCase("post")){
            stringRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //loading.dismiss();
                            Config.ResponceResult = response;
                            //Log.d(Config.TAG,"result : "+response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Config.ResponceResult = error.getMessage();
                            //Log.d(Config.TAG,"error : "+error.getMessage());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //loading.dismiss();
                    return params;
                }
            };
            //Creating a Request Queue
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return Config.ResponceResult;
    }
    public static void getData(final Activity activity, final Context context, final Spinner spinner, String utype){
        //Creating a string request
        String url=Config.API_URL+"app_service.php?type=all_category&name="+utype;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray result = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            result = new JSONArray(response);
                             //Storing the Array of JSON String to our JSON Array
                            //JSONArray result = j.getJSONArray("data");
                            ArrayList<CategoryItem> CatList = new ArrayList<>();
                            //Calling method getStudents to get the students from the JSON Array
                            //Log.d(TAG,result.toString());
                            for(int i=0;i<result.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = result.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    Integer id=json.getInt("id");
                                    String Name= json.getString("name");
                                    CatList.add(new CategoryItem(id, Name));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            spinner.setAdapter(new ArrayAdapter<CategoryItem>(activity, android.R.layout.simple_spinner_dropdown_item, CatList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Config.TAG,error.toString());
                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public static void rateMe(final Context mContext, String id, String uid, float rating, final RatingBar ratingBar){
        String url= Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=demand&total_rate="+rating;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status=response.optString("status");
                            String msg=response.optString("msg");
                            String update_status=response.optString("update_status");
                            String totalrating=response.optString("totalrating");
                            if (status=="success" || status.equalsIgnoreCase("success")){
                                ratingBar.setRating(Float.parseFloat(totalrating));
                            } else {

                            }
                            //Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public static void OpenWallet(Context context)
    {
        String appPackageName="com.mssinfotech.iampro.co";
        String appName = "IAmPro Wallet";
        String packageName = "com.mssinfotech.walletapp";
        openApp(context, appName, packageName);
    }
    public static void OpenBrowser(Context context, String urls)
    {
        String appPackageName="com.mssinfotech.iamprobrowser";
        try {
            if(urls.isEmpty() || urls.equals(""))
                urls="http://www.iampro.co";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
            browserIntent.setPackage("com.mssinfotech.iamprobrowser");
            context.startActivity(browserIntent);
        } catch (ActivityNotFoundException exec) {
            //Toast.makeText(getApplicationContext(),exec.getMessage(),Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        catch (Exception exc)
        {
            Toast.makeText(context.getApplicationContext(),exc.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public static void openApp(Context context, String appName, String packageName) {
        if (isAppInstalled(context, packageName))
            if (isAppEnabled(context, packageName))
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
            else Toast.makeText(context, appName + " app is not enabled.", Toast.LENGTH_SHORT).show();
        else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            Toast.makeText(context, appName + " app is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));

        }
        return false;
    }

    private static boolean isAppEnabled(Context context, String packageName) {
        boolean appStatus = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (ai != null) {
                appStatus = ai.enabled;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return appStatus;
    }
    public static void finishFunction(Context context) {
        Activity activity = (Activity)context;
        activity.finish();
    }
}

