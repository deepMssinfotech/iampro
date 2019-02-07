package com.mssinfotech.iampro.co.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.mssinfotech.iampro.co.adapter.MyImageAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyImageActivity extends AppCompatActivity implements MyImageAdapter.ItemListener {
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private String URL_FEED = "",uid="";
    Intent intent;
     RecyclerView recyclerView;
    MyImageAdapter adapter;
    ArrayList<MyImageModel> itemm= new ArrayList<>();
    //HashSet<String> item_name = new HashSet<>();
    HashMap<String,String> item_name = new HashMap<>();
    ArrayList<SectionImageModel> allSampleData=new ArrayList<>();
    MyImageVideoDataAdapter adapterr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_image));
        intent = getIntent();
        String id = intent.getStringExtra("uid");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        recyclerView=findViewById(R.id.recyclerView);
        tv_category=findViewById(R.id.tv_category);
        userbackgroud = findViewById(R.id.userbackgroud);
        uid= PrefManager.getLoginDetail(this,"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.BANNER_URL+"250/250/"+PrefManager.getLoginDetail(this,"profile_image_gallery");
            String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"img_banner_image");
            username.setText("My Images");
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MyImageActivity.this,ImageImageCroperActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
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
        //getImages();
        getAllAlbum();
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
                            String avatar=Config.AVATAR_URL+"250/250/"+result.optString("profile_image_gallery");
                            String background=Config.AVATAR_URL+"h/250/"+result.optString("img_banner_image");
                            username = findViewById(R.id.username);
                            userimage = findViewById(R.id.userimage);
                            userbackgroud = findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Images");
                            Glide.with(getApplicationContext()).load(background).apply(Config.options_background).into(userbackgroud);
                            Glide.with(getApplicationContext()).load(avatar).apply(Config.options_avatar).into(userimage);

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
        Intent i_signup = new Intent(MyImageActivity.this,AddImageActivity.class);
        MyImageActivity.this.startActivity(i_signup);
    }
    public void getAllAlbum(){
      String url="https://www.iampro.co/api/app_service.php?type=getAlbemsListt&search_type=image&uid="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(!item_name.isEmpty()){
                            item_name.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject student1 = response.getJSONObject(i);
                                String name1=student1.optString("id");
                                 String album_name=student1.optString("album_name");
                                //item_name.add(name1);
                                item_name.put(name1,album_name);
                            }
                            Log.d("allsampledataname",item_name.toString());
                             for (String data:item_name.keySet()){
                                 getImages(data);
                                 Log.d("Keyset",""+data);
                             }
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getImages(final String aid){
         //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
         String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(item_name.get(aid));
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object

                                JSONObject student = response.getJSONObject(i);
                                 String category1=student.getString("category");
                                  String idd=student.optString("id");
                                  String added_byy=student.optString("added_by");

                                   String name1=student.optString("name");

                                    String atype=student.optString("atype");
                                   tv_category.setText(name1);
                                   tv_category.setVisibility(View.GONE);
                                JSONArray jsonArrayPics=student.getJSONArray("pics");
                                Log.d("picssss",jsonArrayPics.toString());

                                 for (int j=0;j<jsonArrayPics.length();j++){
                                     JSONObject pics=jsonArrayPics.getJSONObject(j);
                                      String id=pics.getString("id");

                                     String albemid=pics.optString("albemid");
                                     String name=pics.optString("name");
                                     String category=pics.optString("category");
                                     String albem_type=pics.optString("albem_type");
                                     String image=pics.optString("image");
                                     String udate=pics.optString("udate");
                                     String about_us=pics.optString("about_us");
                                     String group_id=pics.optString("group_id");
                                     String is_featured=pics.optString("is_featured");
                                     //status
                                     String status=pics.optString("status");
                                     String is_block=pics.optString("is_block");
                                     String comments=pics.optString("comments");
                                     String totallike=pics.optString("totallike");
                                     String like_unlike=pics.optString("like_unlike");
                                     String rating=pics.optString("rating");
              item.add(new MyImageModel(id,albemid,name,category,albem_type,image,udate,about_us,group_id,is_featured,status,is_block,comments,totallike,like_unlike,rating,uid));
                                     //singleItem.add(new SingleItemModel(Integer.parseInt(id), name,image,udate,rating,Integer.parseInt(totallike),Integer.parseInt(comments),Integer.parseInt(uid),name,image,"image"));

                                 }
                                //adapter = new MyImageAdapter(getApplicationContext(),item,item_name,MyImageActivity.this);
                                //recyclerView.setAdapter(adapter);
                               // GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                               //recyclerView.setLayoutManager(manager);
                              //recyclerView.setNestedScrollingEnabled(false);
                            }
                            Log.d("allsampledatav",item.toString());

                           /* adapter = new MyImageAdapter(getApplicationContext(),item,item_name,MyImageActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setNestedScrollingEnabled(false); */

                            dm.setAllItemsInSection(item);
                            Log.d("adm",item.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new MyImageVideoDataAdapter(getApplicationContext(), allSampleData,item_name);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapterr);

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
    public void onItemClick(MyImageModel item) {

    }
}
