package com.mssinfotech.iampro.co.user;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.MyProductAdapter;
import com.mssinfotech.iampro.co.adapter.MyProvideAdapter;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProvideActivity extends Fragment implements MyProvideAdapter.ItemListener, SwipeRefreshLayout.OnRefreshListener {
     SwipeRefreshLayout swiperefresh;
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private String uid="",id;
    Intent intent;
    ArrayList<MyProductModel> item = new ArrayList<>();
    MyProvideAdapter adapter;
    RecyclerView recyclerView;
     FloatingActionButton fab;
    Context context;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_my_provide, parent, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (Config.allowRefresh) {
            Config.allowRefresh = false;
            //Toast.makeText(context, "click from BACK", Toast.LENGTH_SHORT).show();
            Fragment frg = null;
            AppCompatActivity activity = (AppCompatActivity) context;
            MyProvideActivity fragment = new MyProvideActivity();
            frg = activity.getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName());
            final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        context = getContext();
        intent = getActivity().getIntent();
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("uid")) {
            id = args.getString("uid").toString();
        }else {
            id = intent.getStringExtra("uid");
        }
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_provide));
        username = view.findViewById(R.id.username);
        userimage = view.findViewById(R.id.userimage);
        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_signup = new Intent(context,AddProvideActivity.class);
                MyProvideActivity.this.startActivity(i_signup);
            }
        });
        if (!PrefManager.isLogin(MyProvideActivity.this.getContext())){
            fab.hide();
        }
        else if (!PrefManager.getLoginDetail(MyProvideActivity.this.getContext(),"id").equalsIgnoreCase(id)) {
            fab.hide();
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        swiperefresh=view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(this);
        // Configure the refreshing colors
        swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        userbackgroud = view.findViewById(R.id.userbackgroud);
        uid= PrefManager.getLoginDetail(context,"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(context,"fname");
            String lname=PrefManager.getLoginDetail(context,"lname");
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(context,"img_url");
            String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(context,"banner_image");
            username.setText("My Provide");
            Glide.with(context).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(context).load(avatar).apply(Config.options_avatar).into(userimage);
            userbackgroud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"banner_image"), null);
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"img_url"), null);
                }
            });
            PrefManager.updateUserData(context,null);
        }else{
            uid= id;
            gteUsrDetail(id);
        }
        IncludeShortMenu includeShortMenu = view.findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(context,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);
        getProvide();
    }
    private void gteUsrDetail(String id){
        String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + id + "&uid=" + uid;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            Log.d(Config.TAG, response);
                            result = new JSONObject(response);
                            String fname=result.optString("fname");
                            String lname=result.optString("lname");
                            final String avatarX=result.getString("avatar");
                            final String backgroundX=result.getString("banner_image");
                            username = view.findViewById(R.id.username);
                            userimage = view.findViewById(R.id.userimage);
                            userbackgroud = view.findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Provide");
                            Glide.with(context).load(Config.AVATAR_URL+"h/250/"+backgroundX).apply(Config.options_background).into(userbackgroud);
                            Glide.with(context).load(Config.AVATAR_URL+"250/250/"+avatarX).apply(Config.options_avatar).into(userimage);
                            userbackgroud.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+backgroundX, null);
                                }
                            });
                            userimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+avatarX, null);
                                }
                            });
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
   /* public void redirect(View v){
        Intent i_signup = new Intent(context,AddProvideActivity.class);
        MyProvideActivity.this.startActivity(i_signup);
    } */

    public void getProvide(){
        String url=Config.API_URL+"app_service.php?type=getall_product&added_by="+uid+"&my_id="+uid+"&search_type=PROVIDE";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!item.isEmpty()){
                            item.clear();
                        }
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Product");
                        dm.setAddedBy(id);
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                int id=student.getInt("id");
                                String idv=String.valueOf(id);
                                int added_by=student.getInt("added_by");

                                int scost=student.getInt("selling_cost");
                                //int pcost=student.getInt("purchese_cost");
                                int pcost=0;
                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/product/" +imagev;
                                String udate=student.getString("udate");
                                int totallike=Integer.parseInt(student.getString("likes"));
                                int comments=student.getInt("comments");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);

                                // String daysago=student.getString("ago");
                                 int like_unlike=student.optInt("like_unlike");
                                String rating=String.valueOf(student.getInt("average_rating"));
                                float ratingv=Float.parseFloat(rating);

                                JSONObject userDetail=student.getJSONObject("user_name");
                                int uid=userDetail.getInt("id");
                                String fname=userDetail.getString("fname");
                                String lname=userDetail.getString("lname");
                                String fullname=fname+"\t"+lname;
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                // singleItem.add(new SingleItemModel(name,image,udate));
                                //allSampleData.add(new DataModel(name,image,udate,categoryv));
                                item.add(new MyProductModel(name,image,udate,categoryv,totallike,comments,scost,ratingv,uid,fullname,avatar,idv,like_unlike));
                            }
                            Log.d("bdm",singleItem.toString());
                            // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav",item.toString());
                            adapter = new MyProvideAdapter(context,item,MyProvideActivity.this);

                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }

    @Override
    public void onItemClick(MyProductModel item) {

    }

    @Override
    public void onRefresh() {
        getProvide();
    }
}
