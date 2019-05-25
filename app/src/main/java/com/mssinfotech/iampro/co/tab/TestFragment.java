package com.mssinfotech.iampro.co.tab;

import android.content.Context;
import android.net.Uri;
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

public class TestFragment extends Fragment implements RecyclerViewAdapter.ItemListener {
    RecyclerView recyclerView;
    ArrayList arrayList= new ArrayList();
     ArrayList allSampleData = new ArrayList();
    RecyclerViewAdapter adapter;
    public TestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

//import com.mssinfotech.iampro.co.model.DataModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        getProvide();
       /* arrayList = new ArrayList();
        arrayList.add(new DataModel("Item 1", android.R.drawable.btn_default, "#09A9FF"));
        arrayList.add(new DataModel("Item 2", android.R.drawable.btn_default, "#3E51B1"));
        arrayList.add(new DataModel("Item 3", android.R.drawable.arrow_up_float, "#673BB7"));
        arrayList.add(new DataModel("Item 4", android.R.drawable.arrow_down_float, "#4BAA50"));
        arrayList.add(new DataModel("Item 5", android.R.drawable.btn_minus, "#F94336"));
        arrayList.add(new DataModel("Item 6", android.R.drawable.alert_dark_frame, "#0A9B88"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), arrayList, this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager); */
    }
    public void getProvide(){
        final String url = Config.API_URL+ "app_service.php?type=all_product_classified&uid=&name=PROVIDE&my_id=";
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
                                arrayList.add(new DataModel(name,image,udate,categoryv));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());

                             //
                            //recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

                            /*arrayList.add(new DataModel("Item 1", android.R.drawable.btn_default, "#09A9FF"));
                            arrayList.add(new DataModel("Item 2", android.R.drawable.btn_default, "#3E51B1"));
                            arrayList.add(new DataModel("Item 3", android.R.drawable.arrow_up_float, "#673BB7"));
                            arrayList.add(new DataModel("Item 4", android.R.drawable.arrow_down_float, "#4BAA50"));
                            arrayList.add(new DataModel("Item 5", android.R.drawable.btn_minus, "#F94336"));
                            arrayList.add(new DataModel("Item 6", android.R.drawable.alert_dark_frame, "#0A9B88")); */

                            adapter = new RecyclerViewAdapter(getContext(), arrayList,TestFragment.this);
                            recyclerView.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);

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

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getContext(), item.getName() + " is clicked", Toast.LENGTH_SHORT).show();

    }
}

