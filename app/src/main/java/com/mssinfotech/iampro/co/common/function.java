package com.mssinfotech.iampro.co.common;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.data.CategoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class function {
    public static boolean isSamePage(String pageName){
        String CurrectPageName=Config.getLayoutName();
        Log.d(Config.TAG,pageName+"=="+CurrectPageName);

        if(pageName.equalsIgnoreCase(CurrectPageName)){
            return true;
        }
        return false;
    }
    public static String executeUrl(final Context context, String type, String url, final Map<String, String> params){
        final ProgressDialog loading = ProgressDialog.show(context,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = null;
        //Log.d(Config.TAG,"url: "+url);
        if(type.equalsIgnoreCase("get")){
            stringRequest = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Config.ResponceResult = response;
                            Log.d(Config.TAG,"result is : "+response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Config.ResponceResult = error.getMessage();
                    //Log.d(Config.TAG,"error : "+error.getMessage());
                }
            });
        }else if(type.equalsIgnoreCase("post")){
            stringRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
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
                    loading.dismiss();
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

}

