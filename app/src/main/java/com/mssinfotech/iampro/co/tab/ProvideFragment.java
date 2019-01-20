package com.mssinfotech.iampro.co.tab;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.common.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProvideFragment extends Fragment implements RecyclerViewAdapter.ItemListener {
    ArrayList allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view;
    RecyclerViewAdapter adapter;
    public ProvideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_provide, container, false);
        //oolbar =view.findViewById(R.id.toolbar);

        //view.findViewById(R.id.title_tv).setTag("Provide");
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //createDummyData();

        getProvide();
        my_recycler_view =view.findViewById(R.id.my_recycler_view);


    }
    /*public void getProvide(){
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
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        }
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
    } */

    public void getProvide(){
        final String url = "https://www.iampro.co/api/app_service.php?type=all_product_classified&uid=&name=PROVIDE&my_id=";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Toast.makeText(getContext(), "getProvide", Toast.LENGTH_SHORT).show();
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response_provide",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        //Toast.makeText(getContext(),"rrrresponse_enterrr:1",Toast.LENGTH_LONG).show();
                        dm.setHeaderTitle("Provide");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                       /* if(!singleItem.isEmpty()){
                            singleItem.clear();
                        } */
                        if(!allSampleData.isEmpty())
                        {
                            allSampleData.clear();
                        }
                        try{
                            int scost=0; int pcost=0;
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                int id=student.getInt("id");
                                int added_by=student.getInt("added_by");
                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/product/" + imagev;
                                String udate=student.getString("udate");
                                String tlike=student.getString("totallike");
                                int totallike=Integer.parseInt(tlike);
                                //int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                scost = student.getInt("selling_cost");
                               /*   if (String.valueOf(student.getInt("selling_cost"))==null){
                                      scost=0;
                                  }
                                  else {
                                      scost = student.getInt("selling_cost");
                                  }
                                if (String.valueOf(student.getInt("purchese_cost"))==null){
                                    pcost=0;
                                }
                                else {
                                    pcost = student.getInt("purchese_cost");
                                } */


                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //String daysago=student.getString("ago");
                                String rating=student.getString("rating");
                                float ratingv=Float.parseFloat(rating);
                                JSONObject userDetail=student.getJSONObject("user_detail");
                                 int uid=userDetail.getInt("id");
                                   String fullname=userDetail.getString("fullname");
                                   String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);

                                // singleItem.add(new SingleItemModel(name,image,udate));
                                //allSampleData.add(new DataModel(name,image,udate,categoryv));
                                allSampleData.add(new DataModel(name,image,udate,categoryv,totallike,comments,scost,pcost,ratingv,uid,fullname,avatar));

                            }
                           // Toast.makeText(getContext(),"rrrresponse_enterrr:2",Toast.LENGTH_LONG).show();
                            //dm.setAllItemsInSection(singleItem);

                            //
                            //recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

                            /*arrayList.add(new DataModel("Item 1", android.R.drawable.btn_default, "#09A9FF"));
                            arrayList.add(new DataModel("Item 2", android.R.drawable.btn_default, "#3E51B1"));
                            arrayList.add(new DataModel("Item 3", android.R.drawable.arrow_up_float, "#673BB7"));
                            arrayList.add(new DataModel("Item 4", android.R.drawable.arrow_down_float, "#4BAA50"));
                            arrayList.add(new DataModel("Item 5", android.R.drawable.btn_minus, "#F94336"));
                            arrayList.add(new DataModel("Item 6", android.R.drawable.alert_dark_frame, "#0A9B88")); */

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData,ProvideFragment.this);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getDemand();
    }
    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getContext(), item.getName()+ " is clicked", Toast.LENGTH_SHORT).show();

    }

}