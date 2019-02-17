package com.mssinfotech.iampro.co.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mssinfotech.iampro.co.adapter.MyDemandAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDemandActivity extends AppCompatActivity implements MyDemandAdapter.ItemListener{

    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private String uid="";
    Intent intent;
    ArrayList<MyProductModel> item = new ArrayList<>();
    MyDemandAdapter adapter;
    RecyclerView recyclerView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_demand);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_demand));
        intent = getIntent();
        context = getApplicationContext();
        String id = intent.getStringExtra("uid");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        recyclerView = findViewById(R.id.recyclerView);
        tv_category=findViewById(R.id.tv_category);
        userbackgroud = findViewById(R.id.userbackgroud);
        uid= PrefManager.getLoginDetail(this,"id");
        if(id == null || id.equals(uid)){
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
            username.setText("My Demand");
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
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
            PrefManager.updateUserData(this,null);
        }else{
            uid= id;
            gteUsrDetail(id);
        }
        IncludeShortMenu includeShortMenu = findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(this,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);
        getDemand();
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
                            username = findViewById(R.id.username);
                            userimage = findViewById(R.id.userimage);
                            userbackgroud = findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Demand");
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+"h/250/"+backgroundX).apply(Config.options_background).into(userbackgroud);
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+"250/250/"+avatarX).apply(Config.options_avatar).into(userimage);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent();
        i.putExtra(Config.PAGE_TAG, Config.PREVIOUS_PAGE_TAG);
        setResult(RESULT_OK, i);
        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "mss popup"+resultCode+"--"+requestCode,  Toast.LENGTH_LONG).show();
    }
    public void redirect(View v){
        Intent i_signup = new Intent(MyDemandActivity.this,AddDemandActivity.class);
        MyDemandActivity.this.startActivity(i_signup);
    }

    public void getDemand(){
        String url=Config.API_URL+"app_service.php?type=getall_product&added_by="+uid+"&my_id="+uid+"&search_type=DEMAND";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                                //tv_category.setText(categoryv);
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
                            adapter = new MyDemandAdapter(MyDemandActivity.this,item,MyDemandActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(MyDemandActivity.this, 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
