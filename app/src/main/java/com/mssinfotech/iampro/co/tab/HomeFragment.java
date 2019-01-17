package com.mssinfotech.iampro.co.tab;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.BuildConfig;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.HomeAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.api.Client;
import com.mssinfotech.iampro.co.api.Service;
import com.mssinfotech.iampro.co.data.AutoFitGridLayoutManager;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.Home;
import com.mssinfotech.iampro.co.model.HomesResponse;
//import com.takusemba.multisnaprecyclerview.BuildConfig;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.utils.Config;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mssinfotech.iampro.co.utils.Config.TAG;

public class HomeFragment extends Fragment implements RecyclerViewAdapter.ItemListener{
    private Toolbar toolbar;

    SectionDataModel dm = new SectionDataModel();
    ArrayList<SingleItemModel> singleItem = new ArrayList<>();
    //ArrayList<SectionDataModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view;

    ArrayList<DataModel> allSampleData=new ArrayList<>();

    RecyclerViewAdapter adapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createDummyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_home, container, false);
         //oolbar =view.findViewById(R.id.toolbar);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // allSampleData = new ArrayList<>();

       /* if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            toolbar.setTitle("G PlayStore");

        } */
        //createDummyData();
       if(!singleItem.isEmpty()){
            singleItem.clear();
        }
        if(!allSampleData.isEmpty()){
            allSampleData.clear();
        }
        callData();
         my_recycler_view =view.findViewById(R.id.my_recycler_view);

      /*  my_recycler_view.setHasFixedSize(true);
         Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter); */

        // Inflate the layout for this fragment

    }

    public void createDummyData() {
        ArrayList<String> type=new ArrayList<>();
        ArrayList<String> name=new ArrayList<>();
        //type
        type.add("get_slider");
        type.add("all_item");
        type.add("getSelectedUser");
        type.add("all_product");
        type.add("all_product_classified");
        type.add("get_slider");
        type.add("get_slider");
        //name
        name.add("TOP_SLIDER");
        //SERVICE_SLIDER
        name.add("SERVICE_SLIDER");
        name.add("image");
        name.add("video");
        name.add("user");
        name.add("product");
        name.add("PROVIDE");
        name.add("DEMAND");

       /* for(int i=0; i<name.size(); i++){
             SectionDataModel dm = new SectionDataModel();
             if (name.get(i).toString().equalsIgnoreCase("TOP_SLIDER")){
                          //getTopSlider(dm );
             }
             else if (name.get(i).toString().equalsIgnoreCase("SERVICE_SLIDER")){
                           //getServiceSlider();
             }
             else if (name.get(i).toString().equalsIgnoreCase("image")){
                       getImage(dm);
             }
             else if (name.get(i).toString().equalsIgnoreCase("video")){
                          getVideo(dm);
             }
             else if (name.get(i).toString().equalsIgnoreCase("user")){
                           getUser(dm);
             }
             else if (name.get(i).toString().equalsIgnoreCase("product")){
                      getProduct(dm);
             }
             else if (name.get(i).toString().equalsIgnoreCase("PROVIDE")){
                       getProvide(dm);
             }
             else if (name.get(i).toString().equalsIgnoreCase("DEMAND")){
                        getDemand(dm);
             }

         } */
     for (int i = 1; i <= 5; i++) {

            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle("Section " + i);
            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
            }
            dm.setAllItemsInSection(singleItem);
            //allSampleData.add(dm);
        }
    }
    public void callData(){

        //getImage();
       for(int i=0;i<6;i++) {
            if (i==0) {
                getImage();
                //return;
            }
          else if (i==1) {
                getVideo();
               // return;
            }
            else if (i==2) {
                getUser();

            }
           else if (i==3) {
                getProduct();

            }
           else if (i==4) {
                getProvide();

            }
           else if (i==5) {
                getDemand();

            }
        }
    }
   /*  public void  getImage(){
         Log.d("rrrresponse_enterrr","rrrresponse_enterrr");
         final String url = "https://www.iampro.co/api/app_service.php?type=all_item&name=image&uid=&my_id=";
         // Initialize a new RequestQueue instance
         RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                         dm.setHeaderTitle("Images ");
                         ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();

                         try{
                             for(int i=0;i<response.length();i++){
                                 // Get current json object
                                 JSONObject student = response.getJSONObject(i);

                                 String name = student.getString("name");
                                 String categoryv=student.getString("category");
                                  String imagev=student.getString("image");
                                  String image= Config.URL_ROOT+"uploads/album/450/500/"+imagev;
                                  String udate=student.getString("udate");
                                  Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                 //SectionDataModel dm = new SectionDataModel();
                                 //dm.setHeaderTitle("Section " + i);
                                 Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                 singleItem.add(new SingleItemModel(name,image,udate));

                             }
                                Log.d("bdm",singleItem.toString());
                             dm.setAllItemsInSection(singleItem);
                             Log.d("adm",singleItem.toString());
                             Log.d("dmm",dm.toString());
                             allSampleData.add(dm);
                             Log.d("allsampledatav", allSampleData.toString());
                             //my_recycler_view.setHasFixedSize(true);
                             Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                             RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            // my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                 my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                            // AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);

                             //my_recycler_view.setLayoutManager(layoutManager);
                             int numberOfColumns = 2;
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));


                             my_recycler_view.setAdapter(adapter);

                             //GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                             //my_recycler_view.setLayoutManager(manager);
                         }
                         catch (JSONException e){
                             e.printStackTrace();
                             Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                             Log.d("catch_f",""+e.getMessage());
                         }
                     }
                 },
                 new com.android.volley.Response.ErrorListener(){
                     @Override
                     public void onErrorResponse(VolleyError error){
                         // Do something when error occurred
                         //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                         Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                         Log.d("verror",error.getMessage());
                     }
                 }
         );
         // Add JsonArrayRequest to the RequestQueue
         requestQueue.add(jsonArrayRequest);
         //getVideo();
     }
     public void getVideo(){
         final String url = "https://www.iampro.co/api/app_service.php?type=all_item&name=video&uid=&my_id=";
         // Initialize a new RequestQueue instance
         RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                         dm.setHeaderTitle("Video ");
                         ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                         try{
                             for(int i=0;i<response.length();i++){
                                 // Get current json object
                                 JSONObject student = response.getJSONObject(i);

                                 String name = student.getString("name");
                                 String categoryv=student.getString("category");
                                 String imagev=student.getString("image");
                                 String image=Config.URL_ROOT + "uploads/v_image/" + imagev;
                                 String udate=student.getString("udate");
                                 Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                 //SectionDataModel dm = new SectionDataModel();
                                 //dm.setHeaderTitle("Section " + i);
                                 Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                 singleItem.add(new SingleItemModel(name,image,udate));

                             }
                             Log.d("bdm",singleItem.toString());
                             dm.setAllItemsInSection(singleItem);
                             Log.d("adm",singleItem.toString());
                             Log.d("dmm",dm.toString());
                             allSampleData.add(dm);
                             Log.d("allsampledatav", allSampleData.toString());
                             my_recycler_view.setHasFixedSize(true);
                             Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                             RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                             my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                             //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                             my_recycler_view.setAdapter(adapter);
                         }
                         catch (JSONException e){
                             e.printStackTrace();
                             Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                             Log.d("catch_f",""+e.getMessage());
                         }
                     }
                 },
                 new com.android.volley.Response.ErrorListener(){
                     @Override
                     public void onErrorResponse(VolleyError error){
                         // Do something when error occurred
                         //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                         Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                         Log.d("verror",error.getMessage());
                     }
                 }
         );
         // Add JsonArrayRequest to the RequestQueue
         requestQueue.add(jsonArrayRequest);
         //getUser();
     }
      public void getUser(){
          final String url = "https://www.iampro.co/api/app_service.php?type=getSelectedUser&limit=15&uid=&my_id=";
          // Initialize a new RequestQueue instance
          RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                          try{
                              for(int i=0;i<response.length();i++){
                                  // Get current json object
                                  JSONObject student = response.getJSONObject(i);

                                  String name = student.getString("fname");
                                  String categoryv=student.getString("identity_type");
                                  String imagev=student.getString("avatar");
                                   String image=Config.AVATAR_URL+"200/200/"+imagev;
                                  String udate=student.getString("udate");
                                  Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                  //SectionDataModel dm = new SectionDataModel();
                                  //dm.setHeaderTitle("Section " + i);
                                  Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                  singleItem.add(new SingleItemModel(name,image,udate));

                              }
                              Log.d("bdm",singleItem.toString());
                              dm.setAllItemsInSection(singleItem);
                              Log.d("adm",singleItem.toString());
                              Log.d("dmm",dm.toString());
                              allSampleData.add(dm);
                              Log.d("allsampledatav", allSampleData.toString());
                              my_recycler_view.setHasFixedSize(true);
                              Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                              RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                              my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                              //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                              my_recycler_view.setAdapter(adapter);
                          }
                          catch (JSONException e){
                              e.printStackTrace();
                              Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                              Log.d("catch_f",""+e.getMessage());
                          }
                      }
                  },
                  new com.android.volley.Response.ErrorListener(){
                      @Override
                      public void onErrorResponse(VolleyError error){
                          // Do something when error occurred
                          //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                          Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                          Log.d("verror",error.getMessage());
                      }
                  }
          );
          // Add JsonArrayRequest to the RequestQueue
          requestQueue.add(jsonArrayRequest);
          //getProduct();
      }
      public void getProduct(){
          final String url = "https://www.iampro.co/api/app_service.php?type=all_product&uid=&name=product&my_id=";
          // Initialize a new RequestQueue instance
          RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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

                                  String name = student.getString("name");
                                  String categoryv=student.getString("category");
                                  String imagev=student.getString("image");
                                   String image=Config.URL_ROOT + "uploads/product/" +imagev;
                                  String udate=student.getString("udate");
                                  Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                  //SectionDataModel dm = new SectionDataModel();
                                  //dm.setHeaderTitle("Section " + i);
                                  Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                  singleItem.add(new SingleItemModel(name,image,udate));

                              }
                              Log.d("bdm",singleItem.toString());
                              dm.setAllItemsInSection(singleItem);
                              Log.d("adm",singleItem.toString());
                              Log.d("dmm",dm.toString());
                              allSampleData.add(dm);
                              Log.d("allsampledatav", allSampleData.toString());
                              my_recycler_view.setHasFixedSize(true);
                              Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                              RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                              my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                              //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                              my_recycler_view.setAdapter(adapter);
                          }
                          catch (JSONException e){
                              e.printStackTrace();
                              Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                              Log.d("catch_f",""+e.getMessage());
                          }
                      }
                  },
                  new com.android.volley.Response.ErrorListener(){
                      @Override
                      public void onErrorResponse(VolleyError error){
                          // Do something when error occurred
                          //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                          Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                          Log.d("verror",error.getMessage());
                      }
                  }
          );
          // Add JsonArrayRequest to the RequestQueue
          requestQueue.add(jsonArrayRequest);
          //getProvide();
      }
       public void getProvide(){
           final String url = "https://www.iampro.co/api/app_service.php?type=all_product_classified&uid=&name=PROVIDE&my_id=";
           // Initialize a new RequestQueue instance
           RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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

                                   String name = student.getString("name");
                                   String categoryv=student.getString("category");
                                   String imagev=student.getString("image");
                                    String image=Config.URL_ROOT + "uploads/product/" + imagev;
                                   String udate=student.getString("udate");
                                   Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                   //SectionDataModel dm = new SectionDataModel();
                                   //dm.setHeaderTitle("Section " + i);
                                   Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                   singleItem.add(new SingleItemModel(name,image,udate));

                               }
                               Log.d("bdm",singleItem.toString());
                               dm.setAllItemsInSection(singleItem);
                               Log.d("adm",singleItem.toString());
                               Log.d("dmm",dm.toString());
                               allSampleData.add(dm);
                               Log.d("allsampledatav", allSampleData.toString());
                               my_recycler_view.setHasFixedSize(true);
                               Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                               RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                               my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                               //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                               my_recycler_view.setAdapter(adapter);
                           }
                           catch (JSONException e){
                               e.printStackTrace();
                               Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                               Log.d("catch_f",""+e.getMessage());
                           }
                       }
                   },
                   new com.android.volley.Response.ErrorListener(){
                       @Override
                       public void onErrorResponse(VolleyError error){
                           // Do something when error occurred
                           //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                           Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                           Log.d("verror",error.getMessage());
                       }
                   }
           );
           // Add JsonArrayRequest to the RequestQueue
           requestQueue.add(jsonArrayRequest);
           //getDemand();
       }
        public void getDemand(){
            final String url = "https://www.iampro.co/api/app_service.php?type=all_product_classified&uid=&name=DEMAND&my_id=";
            // Initialize a new RequestQueue instance
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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

                                    String name = student.getString("name");
                                    String categoryv=student.getString("category");
                                    String imagev=student.getString("image");
                                    String image=Config.URL_ROOT + "uploads/product/" +imagev;
                                    String udate=student.getString("udate");
                                    Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                    //SectionDataModel dm = new SectionDataModel();
                                    //dm.setHeaderTitle("Section " + i);
                                    Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                    singleItem.add(new SingleItemModel(name,image,udate));

                                }
                                Log.d("bdm",singleItem.toString());
                                dm.setAllItemsInSection(singleItem);
                                Log.d("adm",singleItem.toString());
                                Log.d("dmm",dm.toString());
                                allSampleData.add(dm);
                                Log.d("allsampledatav", allSampleData.toString());
                                my_recycler_view.setHasFixedSize(true);
                                Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                                RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                                my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                my_recycler_view.setAdapter(adapter);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("catch_f",""+e.getMessage());
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            // Do something when error occurred
                            //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("verror",error.getMessage());
                        }
                    }
            );
            // Add JsonArrayRequest to the RequestQueue
            requestQueue.add(jsonArrayRequest);
        } */

    public void  getImage(){
        Log.d("rrrresponse_enterrr","rrrresponse_enterrr");
        final String url = "https://www.iampro.co/api/app_service.php?type=all_item&name=image&uid=&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                        dm.setHeaderTitle("Images ");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();

                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT+"uploads/album/450/500/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                singleItem.add(new SingleItemModel(name,image,udate));
                                allSampleData.add(new DataModel(name,image,udate,categoryv));

                            }
                            Log.d("bdm",singleItem.toString());
                            //dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            //RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            //
                            //RecyclerViewAdapter adapter= new RecyclerViewAdapter(getContext(), allSampleData,ImageFragment.this);
                            //my_recycler_view.setAdapter(adapter);


                            //

                            /* my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            my_recycler_view.setAdapter(adapter); */

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData, HomeFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getVideo();
    }

    public void getVideo(){
        final String url = "https://www.iampro.co/api/app_service.php?type=all_item&name=video&uid=&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                        dm.setHeaderTitle("Video ");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();

                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/v_image/" + imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                // Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(name,image,udate));
                                allSampleData.add(new DataModel(name,image,udate,categoryv));
                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            // allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());


                          /*  my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter); */

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData,HomeFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getUser();
    }
    public void getUser(){
        final String url = "https://www.iampro.co/api/app_service.php?type=getSelectedUser&limit=15&uid=&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("fname");
                                String categoryv=student.getString("identity_type");
                                String imagev=student.getString("avatar");
                                String image= Config.AVATAR_URL+"200/200/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(name,image,udate));
                                allSampleData.add(new DataModel(name,image,udate,categoryv));
                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());

                            /*my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter); */

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData,HomeFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProduct();
    }

    public void getProduct(){
        final String url = "https://www.iampro.co/api/app_service.php?type=all_product&uid=&name=product&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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

                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/product/" +imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(name,image,udate));
                                allSampleData.add(new DataModel(name,image,udate,categoryv));
                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            /*
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter);
                             */
                            adapter = new RecyclerViewAdapter(getContext(), allSampleData,HomeFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }

    public void getProvide(){
        final String url = "https://www.iampro.co/api/app_service.php?type=all_product_classified&uid=&name=PROVIDE&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/product/" + imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(name,image,udate));
                                allSampleData.add(new DataModel(name,image,udate,categoryv));

                            }
                            //dm.setAllItemsInSection(singleItem);

                            //
                            //recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

                            /*arrayList.add(new DataModel("Item 1", android.R.drawable.btn_default, "#09A9FF"));
                            arrayList.add(new DataModel("Item 2", android.R.drawable.btn_default, "#3E51B1"));
                            arrayList.add(new DataModel("Item 3", android.R.drawable.arrow_up_float, "#673BB7"));
                            arrayList.add(new DataModel("Item 4", android.R.drawable.arrow_down_float, "#4BAA50"));
                            arrayList.add(new DataModel("Item 5", android.R.drawable.btn_minus, "#F94336"));
                            arrayList.add(new DataModel("Item 6", android.R.drawable.alert_dark_frame, "#0A9B88")); */

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData,HomeFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                           /* allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter); */
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getDemand();
    }

    public void getDemand(){
        final String url = "https://www.iampro.co/api/app_service.php?type=all_product_classified&uid=&name=DEMAND&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/product/" +imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                // singleItem.add(new SingleItemModel(name,image,udate));
                                allSampleData.add(new DataModel(name,image,udate,categoryv));

                            }
                            Log.d("bdm",singleItem.toString());
                            //dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            // allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());

                           /* RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter); */

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData,HomeFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);


                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getContext(), item.getName() + " is clicked", Toast.LENGTH_SHORT).show();

    }

}

