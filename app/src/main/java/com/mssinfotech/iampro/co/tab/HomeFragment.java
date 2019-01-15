package com.mssinfotech.iampro.co.tab;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.BuildConfig;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.HomeAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.api.Client;
import com.mssinfotech.iampro.co.api.Service;
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
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.mssinfotech.iampro.co.utils.Config.TAG;

public class HomeFragment extends Fragment{
    private Toolbar toolbar;


    ArrayList<SectionDataModel> allSampleData;
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
        allSampleData = new ArrayList<>();

       /* if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            toolbar.setTitle("G PlayStore");

        } */
        createDummyData();

        RecyclerView my_recycler_view =view.findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);

        // Inflate the layout for this fragment

    }

   /* private void loadPopular(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            Call<HomesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<HomesResponse>() {
                @Override
                public void onResponse(Call<HomesResponse> call, Response<HomesResponse> response) {
                    List<Home> movies = response.body().getResults();
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            HomeAdapter firstAdapter = new HomeAdapter(getContext(), movies);
                            MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView)findViewById(R.id.first_recycler_view);
                            LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            firstRecyclerView.setLayoutManager(firstManager);
                            firstRecyclerView.setAdapter(firstAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<HomesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTopRated(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            Call<HomesResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<HomesResponse>() {
                @Override
                public void onResponse(Call<HomesResponse> call, Response<HomesResponse> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            List<Home> movies = response.body().getResults();

                            HomeAdapter secondAdapter = new HomeAdapter(getContext(), movies);
                            MultiSnapRecyclerView secondRecyclerView =(MultiSnapRecyclerView) findViewById(R.id.second_recycler_view);
                            LinearLayoutManager secondManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            secondRecyclerView.setLayoutManager(secondManager);
                            secondRecyclerView.setAdapter(secondAdapter);

                        }
                    }
                }

                @Override
                public void onFailure(Call<HomesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/

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

            allSampleData.add(dm);

        }
    }
     public void  getImage( final SectionDataModel dm){
         //SectionDataModel dm = new SectionDataModel();
         //dm.setHeaderTitle("Section " + i);

         final String url = "https://www.iampro.co/api/app_service.php?type=all_item&name=image&uid=&my_id=";
         JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                 url, null,
                 new com.android.volley.Response.Listener<JSONObject>() {

                     @Override
                     public void onResponse(JSONObject response) {
                         Log.d(TAG, response.toString());
                         Toast.makeText(getContext(),"Response:"+response.toString(),Toast.LENGTH_LONG).show();
                         int j=1;
                           // while()
                         JSONArray keys =response.names();
                         Log.d("home_key",""+keys);
                         for (int i = 0; i < keys.length (); i++) {
                              try {
                                  dm.setHeaderTitle("Section " + i);
                                  //String key = keys.getString(i); // Here's your key
                                  //String value = response.getString(key); // Here's your value
                                  JSONObject jsonObject=keys.getJSONObject(i);
                                   String name=jsonObject.getString("name");
                                   //Log.d("getImage",""+key+"--"+value+"--"+name);
                                  Log.d("getImage",""+"--"+name);

                                  Toast.makeText(getContext(), "name:"+name, Toast.LENGTH_SHORT).show();
                              }
                              catch (Exception e){
                                  Toast.makeText(getContext(),"catch:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                  Log.d("catch_getAll",""+e.getMessage());

                              }

                         }
                         //dm.setHeaderTitle("Section " + i);
                         //hideProgressDialog();
                     }
                 }, new com.android.volley.Response.ErrorListener() {

             @Override
             public void onErrorResponse(VolleyError error) {
                 VolleyLog.d(TAG, "Error: " + error.getMessage());
                 Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                // hideProgressDialog();
             }
         }) {

             /**
              * Passing some request headers
              * */
             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("Content-Type", "application/json");
                 return headers;
             }

             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<String, String>();


                 return params;
             }

         };

       RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjReq);

     }
     public void getVideo(SectionDataModel dm){

     }
      public void getUser(SectionDataModel dm){

      }
      public void getProduct(SectionDataModel dm){

      }
       public void getProvide(SectionDataModel dm){

       }
        public void getDemand(SectionDataModel dm){

        }
    public void callApi(){

    }
}

