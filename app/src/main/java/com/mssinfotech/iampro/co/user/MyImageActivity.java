package com.mssinfotech.iampro.co.user;

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
import com.mssinfotech.iampro.co.adapter.MyImageAdapter;
import com.mssinfotech.iampro.co.adapter.MyProductAdapter;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.model.MyImageModel;
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

public class MyImageActivity extends AppCompatActivity implements MyImageAdapter.ItemListener {

    ImageView userbackgroud,changeImage;
    CircleImageView userimage;
    TextView username;
    private String URL_FEED = "",uid="";
    Intent intent;
     RecyclerView recyclerView;
    MyImageAdapter adapter;
    ArrayList<MyImageModel> item = new ArrayList<>();
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
        userbackgroud = findViewById(R.id.userbackgroud);
        uid= PrefManager.getLoginDetail(this,"id");
        changeImage = findViewById(R.id.changeImage);
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.BANNER_URL+"250/250/"+PrefManager.getLoginDetail(this,"profile_image_gallery");
            String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"img_banner_image");
            username.setText("My Images");
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            changeImage.setVisibility(View.VISIBLE);
            changeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MyImageActivity.this,ImageImageCroperActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            changeImage.setVisibility(View.GONE);
            uid= id;
            gteUsrDetail(id);
        }
        IncludeShortMenu includeShortMenu = findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(this,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);
        getImages();
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
                            String fname=result.getString("fname");
                            String lname=result.getString("lname");
                            String avatar=Config.AVATAR_URL+"250/250/"+result.getString("profile_image_gallery");
                            String background=Config.AVATAR_URL+"h/250/"+result.getString("img_banner_image");
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
    public void getImages(){
         String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
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
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                JSONArray jsonArrayPics=student.getJSONArray("pics");
                                Log.d("picssss",jsonArrayPics.toString());

                                JSONObject picss= jsonArrayPics.getJSONObject(0);
                                Log.d("picssss11",""+picss.toString());
                                String idd=picss.getString("albemid");
                                Log.d("picssss1",""+idd);

                                 for (int j=0;j<jsonArrayPics.length();j++){
                                     JSONObject pics=jsonArrayPics.getJSONObject(j);
                                      String id=pics.getString("id");

                                     String albemid=pics.getString("albemid");
                                     String name=pics.getString("name");
                                     String category=pics.getString("category");
                                     String albem_type=pics.getString("albem_type");
                                     String image=pics.getString("image");
                                     String udate=pics.getString("udate");
                                     String about_us=pics.getString("about_us");
                                     String group_id=pics.getString("group_id");
                                     String is_featured=pics.getString("is_featured");
                                     //status
                                     String status=pics.getString("status");
                                     String is_block=pics.getString("is_block");
                                     String comments=pics.getString("comments");
                                     String totallike=pics.getString("totallike");
                                     String like_unlike=pics.getString("like_unlike");
                                       String rating=pics.optString("rating");
                                       item.add(new MyImageModel(id,albemid,name,category,albem_type,image,udate,about_us,group_id,is_featured,status,is_block,comments,totallike,like_unlike,rating,uid));
                                 }
                            }
                            Log.d("bdm",singleItem.toString());
                            // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("allsampledatav",item.toString());
                            adapter = new MyImageAdapter(getApplicationContext(),item,MyImageActivity.this);

                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setNestedScrollingEnabled(false);
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
