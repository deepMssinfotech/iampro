package com.mssinfotech.iampro.co.common;


import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                            ArrayList<String> students = new ArrayList<String>();
                            //Calling method getStudents to get the students from the JSON Array
                            //Log.d(TAG,result.toString());
                            for(int i=0;i<result.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = result.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    students.add(json.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            spinner.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, students));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public static String executeUrl(final Context context, String type, String url, final Map<String, String> params){
        if(type.equalsIgnoreCase("get")){

            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Config.ResponceResult = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Config.ResponceResult = error.getMessage().toString();
                }
            });
            //Creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            //Adding request to the queue
            requestQueue.add(stringRequest);
        }else if(type.equalsIgnoreCase("post")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Config.ResponceResult = response;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Config.ResponceResult = error.getMessage().toString();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            //Adding request to the queue
            requestQueue.add(stringRequest);
        }
        return Config.ResponceResult;
    }

}

