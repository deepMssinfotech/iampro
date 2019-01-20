package com.mssinfotech.iampro.co.tab;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
//import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
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

public class ImageFragment extends Fragment implements RecyclerViewAdapter.ItemListener{
    ArrayList<DataModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view;
    RecyclerViewAdapter adapter;
    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_image, container, false);
        // Inflate the layout for this fragment
        //view.findViewById(R.id.title_tv).setTag("Image");
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // allSampleData = new ArrayList<>();


        //createDummyData();
        //callData();
        getImage();
        my_recycler_view =view.findViewById(R.id.my_recycler_view);

      /*  my_recycler_view.setHasFixedSize(true);
         Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter); */

        // Inflate the layout for this fragment

    }
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
                                int id=student.getInt("id");
                               // int album_id=student.getInt("album_id");

                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT+"uploads/album/450/500/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");

                                String daysago=student.getString("ago");


                                JSONObject userDetail=student.getJSONObject("user_detail");

                                int uid=userDetail.getInt("id");
                                String fullname=userDetail.getString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                singleItem.add(new SingleItemModel(name,image,udate));
                                //allSampleData.add(new DataModel(name,image,udate,categoryv));
                                allSampleData.add(new DataModel(name,image,udate,categoryv,totallike,comments,daysago));

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

                            adapter = new RecyclerViewAdapter(getContext(), allSampleData, ImageFragment.this);
                            my_recycler_view.setItemAnimator(new DefaultItemAnimator());
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
    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getContext(), item.getName() + " is clicked", Toast.LENGTH_SHORT).show();

    }

}