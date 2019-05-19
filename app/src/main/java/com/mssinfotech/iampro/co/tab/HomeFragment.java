package com.mssinfotech.iampro.co.tab;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.BuildConfig;
import com.mssinfotech.iampro.co.LoadingActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchedActivity;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.adapter.HomeAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.api.Client;
import com.mssinfotech.iampro.co.api.Service;
import com.mssinfotech.iampro.co.common.SlidingImage_Adapter;
import com.mssinfotech.iampro.co.data.AutoFitGridLayoutManager;
import com.mssinfotech.iampro.co.data.ImageModel;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.Home;
import com.mssinfotech.iampro.co.model.HomesResponse;
//import com.takusemba.multisnaprecyclerview.BuildConfig;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mssinfotech.iampro.co.common.Config.TAG;

public class HomeFragment extends Fragment implements UserDataAdapter.ItemListener{
    private Toolbar toolbar;
    int uid;
    SectionDataModel dm = new SectionDataModel();
    ArrayList<SingleItemModel> singleItem = new ArrayList<>();
    ArrayList<SectionDataModel> allSampleData=new ArrayList<>();
    //ArrayList<DataModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view,recycler_view_video,recycler_view_user,recycler_view_product,recycler_view_provide,recycler_view_demand,recycler_view_list;
    RecyclerViewAdapter adapter;
    RecyclerViewDataAdapter adapterr;
    UserDataAdapter user_adapter;
    ArrayList<UserModel> userSampleData=new ArrayList<>();
    View views;
    Context mContext;
//sliderr
    private static ViewPager mPager,mPager2;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<com.mssinfotech.iampro.co.data.ImageModel> imageModelArrayList,imageModelArrayList2;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        //createDummyData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_home, container, false);
        views=view;
         //oolbar =view.findViewById(R.id.toolbar);

        return view;
    }
   /* private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            ImageModel imageModel = new ImageModel();
            //imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }*/
    private void init() {

        mPager = views.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(mContext,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)views.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

     private void getTopSlider(){
        final String url=Config.API_URL+ "index.php?type=get_slider&name=TOP_SLIDER";
         RequestQueue requestQueue = Volley.newRequestQueue(mContext);
         // Initialize a new JsonArrayRequest instance
         JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                 Request.Method.GET,
                 url,
                 null,
                 new com.android.volley.Response.Listener<JSONArray>() {
                     @Override
                     public void onResponse(JSONArray response) {
                         // pDialog.dismiss();
                         Log.d("responsef",response.toString());
                             if (!imageModelArrayList.isEmpty()){
                                 imageModelArrayList.clear();
                             }
                         try{
                             for(int i=0;i<response.length();i++){
                                 // Get current json object
                                 JSONObject student = response.getJSONObject(i);

                                 String id = student.optString("id");
                                 String heading = student.optString("heading");

                                 String slider_type=student.optString("slider_type");
                                  //id ,heading,slider_type,link,image,slider_image,status,language,lorder,no,index,mindex
                                 String link=student.optString("link");
                                 String imagev=student.optString("image");
                                 String slider_image=student.optString("slider_image");
                                 String status=student.optString("status");
                                 String language=student.optString("language");
                                 String lorder=student.optString("lorder");
                                  int no=student.optInt("no");
                                  int index=student.optInt("index");
                                  int mindex=student.optInt("mindex");

                                 String image= Config.URL_ROOT+"uploads/album/300/250/"+imagev;
                                 imageModelArrayList.add(new ImageModel(id,heading,slider_type,link,imagev,slider_image,status,language,lorder,no,index,String.valueOf(mindex)));
                                 //singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"image"));
                             }
                             init();
                         }
                         catch (JSONException e){
                             //pDialog.dismiss();
                             e.printStackTrace();
                             Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                             Log.d("catch_f",""+e.getMessage());
                         }
                     }
                 },
                 new com.android.volley.Response.ErrorListener(){
                     @Override
                     public void onErrorResponse(VolleyError error){
                         //pDialog.dismiss();
                         // Do something when error occurred
                         Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                         Log.d("verror",""+error.getMessage());
                     }
                 }
         );
         jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         // Add JsonArrayRequest to the RequestQueue
         requestQueue.add(jsonArrayRequest);
     }
    private void init2() {

        mPager2 = views.findViewById(R.id.pager2);
        mPager2.setAdapter(new SlidingImage_Adapter(mContext,imageModelArrayList2));

        CirclePageIndicator indicator2 = (CirclePageIndicator)views.findViewById(R.id.indicator2);

        indicator2.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator2.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList2.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager2.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
     public void getSecondSlider(){
         final String url=Config.API_URL+ "index.php?type=get_slider&name=SERVICE_SLIDER";
         RequestQueue requestQueue = Volley.newRequestQueue(mContext);
         // Initialize a new JsonArrayRequest instance
         JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                 Request.Method.GET,
                 url,
                 null,
                 new com.android.volley.Response.Listener<JSONArray>() {
                     @Override
                     public void onResponse(JSONArray response) {
                         // pDialog.dismiss();
                         Log.d("responsef",response.toString());
                         if (!imageModelArrayList2.isEmpty()){
                             imageModelArrayList2.clear();
                         }

                         try{
                             for(int i=0;i<response.length();i++){
                                 // Get current json object
                                 JSONObject student = response.getJSONObject(i);

                                 String id = student.optString("id");
                                 String heading = student.optString("heading");

                                 String slider_type=student.optString("slider_type");
                                 //id ,heading,slider_type,link,image,slider_image,status,language,lorder,no,index,mindex
                                 String link=student.optString("link");
                                 String imagev=student.optString("image");
                                 String slider_image=student.optString("slider_image");
                                 String status=student.optString("status");
                                 String language=student.optString("language");
                                 String lorder=student.optString("lorder");
                                 int no=student.optInt("no");
                                 int index=student.optInt("index");
                                 int mindex=student.optInt("mindex");

                                 String image= Config.URL_ROOT+"uploads/album/300/250/"+imagev;
                                 imageModelArrayList2.add(new ImageModel(id,heading,slider_type,link,imagev,slider_image,status,language,lorder,no,index,String.valueOf(mindex)));
                                 //singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"image"));
                             }
                             init2();
                         }
                         catch (JSONException e){
                             //pDialog.dismiss();
                             e.printStackTrace();
                             Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                             Log.d("catch_f",""+e.getMessage());
                         }
                     }
                 },
                 new com.android.volley.Response.ErrorListener(){
                     @Override
                     public void onErrorResponse(VolleyError error){
                         //pDialog.dismiss();
                         // Do something when error occurred
                         Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                         Log.d("verror",""+error.getMessage());
                     }
                 }
         );
         jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         // Add JsonArrayRequest to the RequestQueue
         requestQueue.add(jsonArrayRequest);

     }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // allSampleData = new ArrayList<>();
        //createDummyData();
        if(!singleItem.isEmpty()){
            singleItem.clear();
        }
        if(!allSampleData.isEmpty()){
            allSampleData.clear();
        }
        if(PrefManager.isLogin(mContext))
        uid= Integer.parseInt(PrefManager.getLoginDetail(mContext,"id"));
        //callData();
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //getTopSlider();
            }
        }, 3500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //init();
            }
        }, 3500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3500); */


        //my_recycler_view,recycler_view_video,recycler_view_user,recycler_view_product,recycler_view_provide,recycler_view_demand
         my_recycler_view =view.findViewById(R.id.my_recycler_view);
        recycler_view_list=view.findViewById(R.id.recycler_view_list);
        imageModelArrayList=new ArrayList<>();
        imageModelArrayList2=new ArrayList<>();
       /* recycler_view_video=view.findViewById(R.id.video_rv_view);
        recycler_view_user=view.findViewById(R.id.user_rv_view);
        recycler_view_product=view.findViewById(R.id.product_rv_view);
        recycler_view_provide=view.findViewById(R.id.provide_rv_view);
        recycler_view_demand=view.findViewById(R.id.demand_rv_view);

        my_recycler_view.setNestedScrollingEnabled(false);
        recycler_view_video.setNestedScrollingEnabled(false);
        recycler_view_user.setNestedScrollingEnabled(false);
        recycler_view_product.setNestedScrollingEnabled(false);
        recycler_view_provide.setNestedScrollingEnabled(false);
        recycler_view_demand.setNestedScrollingEnabled(false); */
      /*  my_recycler_view.setHasFixedSize(true);
         Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter); */

        // Inflate the layout for this fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getTopSlider();
            }
        }, 500);
        //init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSecondSlider();
            }
        }, 500);

      //init2();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getImage();
            }
        }, 500);
    }
    public void  getImage(){
        final String url = Config.API_URL+ "app_service.php?type=all_item&name=image&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);



        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       // pDialog.dismiss();
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Images");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String type = student.optString("type");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("image");
                                //String image= Config.URL_ROOT+"uploads/album/450/500/"+imagev;
                                String image= Config.URL_ROOT+"uploads/album/300/250/"+imagev;
                                String udate=student.optString("udate");
                                //Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int isliked=student.getInt("like_unlike");
                                int comments=student.getInt("comments");

                                 String rating=student.optString("rating");

                                JSONObject userDetail=student.optJSONObject("user_detail");

                                int uid=userDetail.getInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.optString("avatar");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(mContext,"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();

                                //singleItem.add(new SingleItemModel(name,image,udate));
                                //singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"image"));
                                singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"image"));
                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                             adapterr = new RecyclerViewDataAdapter(mContext, allSampleData);

                            // my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                           // my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

                            // AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);

                            //my_recycler_view.setLayoutManager(layoutManager);


                            my_recycler_view.setAdapter(adapterr);
                            int numberOfColumns = 2;
                            GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
                            my_recycler_view.setLayoutManager(manager);

                            /*GridLayoutManager recycler_view_list = new GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
                            my_recycler_view.setLayoutManager(manager); */

                            //adapter.notifyDataSetChanged();
                          //recycler_view_list


                            getVideo();
                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //pDialog.dismiss();
                        // Do something when error occurred
                        //Snackbar.make(mContext,"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getVideo();
    }
    public void getVideo(){
        final String url = Config.API_URL+ "app_service.php?type=all_item&name=video&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //pDialog.dismiss();
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Video");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("v_image");
                                String image=Config.URL_ROOT + "uploads/v_image/300/250/" + imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);

                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                  String rating=student.optString("rating");
                                JSONObject userDetail=student.optJSONObject("user_detail");

                                int uid=userDetail.getInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.getString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");
                                int isliked=student.getInt("like_unlike");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(mContext,"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"video"));
                                singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"video"));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                               adapterr = new RecyclerViewDataAdapter(mContext, allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);

                            //getUser();
                                   getProduct();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(mContext,"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
       // jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getUser();
    }
    public void getUser(){
        final String url = Config.API_URL+ "app_service.php?type=getSelectedUser&limit=15&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("User");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                       /* if(!singleItem.isEmpty()){
                            singleItem.clear();
                        } */
                        if(!userSampleData.isEmpty()){
                            userSampleData.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("fname");
                                String identity_type=student.optString("identity_type");
                                String category=student.optString("category");
                                String imagev=student.optString("avatar");
                                String image= Config.AVATAR_URL+"300/250/"+imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(mContext,"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(name,image,udate));
                                userSampleData.add(new UserModel(uid,name,image,udate,category));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("usersampledatav", userSampleData.toString());
                            // my_recycler_view.setHasFixedSize(true);
                            Log.d("userSampleDatas",""+userSampleData.size()+"--"+userSampleData.toString());


                            user_adapter = new UserDataAdapter(mContext,userSampleData,HomeFragment.this);
                            my_recycler_view.setAdapter(user_adapter);

                            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                            getProduct();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(mContext,"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProduct();
    }

    public void getProduct(){
        final String url = Config.API_URL+ "app_service.php?type=all_product&uid="+uid+"&name=product&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Product");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.optString("image");
                                String image=Config.URL_ROOT + "uploads/product/300/250/" +imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(mContext,"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();

                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                JSONObject userDetail=student.optJSONObject("user_detail");
                                int uid=userDetail.getInt("id");
                                int id=student.getInt("id");
                                int isliked=student.getInt("like_unlike");
                                String rating=student.optString("rating");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");

                                singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"product"));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(mContext, allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);

                            getProvide();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(mContext,"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getProvide(){
        final String url = Config.API_URL+ "app_service.php?type=all_product_classified&uid="+uid+"&name=PROVIDE&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Provide");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("image");
                                String image=Config.URL_ROOT + "uploads/product/300/250/" + imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(mContext,"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                String rating=student.optString("rating");
                                int comments=student.getInt("comments");
                                JSONObject userDetail=student.optJSONObject("user_detail");
                                int uid=userDetail.optInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.optString("avatar");
                                int isliked=student.getInt("like_unlike");
                                singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"provide"));


                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(mContext, allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);

                            getDemand();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(mContext,"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getDemand();
    }
    public void getDemand(){
        final String url = Config.API_URL+ "app_service.php?type=all_product_classified&uid="+uid+"&name=DEMAND&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Demand");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("image");
                                String image=Config.URL_ROOT + "uploads/product/300/250/" +imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(mContext,"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();

                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                String rating=student.optString("rating");
                                JSONObject userDetail=student.optJSONObject("user_detail");
                                int uid=userDetail.optInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.optString("avatar");
                                int isliked=student.getInt("like_unlike");
                                singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"demand"));


                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                           adapterr = new RecyclerViewDataAdapter(mContext, allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);
                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                         //pDialog.dismiss();
                        // Do something when error occurred
                        //Snackbar.make(mContext,"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mContext, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(UserModel item) {
        //Toast.makeText(mContext, item.getName()+ " is clicked", Toast.LENGTH_SHORT).show();
    }
   /* @Override
    public void onItemClick(DataModel item) {
        Toast.makeText(mContext, item.getName() + " is clicked", Toast.LENGTH_SHORT).show();

    } */
}

