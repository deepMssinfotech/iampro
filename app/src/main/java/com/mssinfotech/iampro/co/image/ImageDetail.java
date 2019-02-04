package com.mssinfotech.iampro.co.image;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.Img_Video_Details;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;

public class ImageDetail extends AppCompatActivity implements Img_Video_Details.ItemListener {
    public  int uid,id;
    String type="";
    ArrayList<ImageDetailModel> myData=new ArrayList<>();

    TextView tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category;
     ImageView imageView_user,imageView_icon,iv_comments,image;
    VideoView videoView;
      RatingBar ratingBar;
       RecyclerView recycler_view_review_detail;
    public static final String IMAGE_TYPE="image";
    public static final String VIDEO_TYPE="video";
       Img_Video_Details adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        tv_about_tag=findViewById(R.id.tv_about_tag);
        tv_about_msg=findViewById(R.id.tv_about_msg);
        fullname=findViewById(R.id.fullname);
        udate=findViewById(R.id.udate);
        tv_comments=findViewById(R.id.tv_comments);
        tv_totallike=findViewById(R.id.tv_totallike);
        name=findViewById(R.id.name);
        category=findViewById(R.id.category);

        imageView_user=findViewById(R.id.imageView_user);
        imageView_icon=findViewById(R.id.imageView_icon);
        iv_comments=findViewById(R.id.iv_comments);
        image=findViewById(R.id.image);
        videoView=findViewById(R.id.video);
         ratingBar=findViewById(R.id.ratingBar);
        recycler_view_review_detail=findViewById(R.id.recycler_view_review_detail);
        uid=getIntent().getExtras().getInt("uid");
        id=getIntent().getExtras().getInt("id");
         type=getIntent().getExtras().getString("type");
         if(type.equalsIgnoreCase("image")){
             getImageDetail();
         }
        else if (type.equalsIgnoreCase("video")){
             getVideoDetail();
         }

    }
    protected void getImageDetail(){
        String url="https://www.iampro.co/api/app_service.php?type=get_image_detail&id="+id+"&update_type="+type+"&uid=1&login_id="+uid+"&my_id="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(ImageDetail.this);

        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Prod_detail",response.toString());
                        // Process the JSON
                        try{
                            JSONObject responses=new JSONObject(response);

                            int id =responses.optInt("id");
                            int albemid =responses.optInt("albemid");
                            String namev =responses.optString("name");
                            int categoryv=responses.optInt("category");
                            int albem_type=responses.optInt("albem_type");
                            String imagee=responses.optString("image");
                             String category_name=responses.optString("category_name");
                              //https://www.iampro.co/uploads/album/w/500/45.png
                            String avatar_url=Config.ALBUM_URL+imagee;
                            String udatev=responses.optString("udate");
                            String about_us=responses.optString("about_us");
                             int group_id=responses.optInt("group_id");
                             int is_featured=responses.optInt("is_featured");
                             int status=responses.optInt("status");
                             String is_block=responses.optString("is_block");
                              int total_group_image=responses.optInt("total_group_image");
                                int like_unlike=responses.optInt("like_unlike");
                                 Double rating=responses.optDouble("rating");
                                  int totallike=responses.optInt("totallike");
                                   int comments=responses.optInt("comments");
                                   //tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category
                            tv_about_msg.setText(about_us);
                            udate.setText(udatev);
                            tv_comments.setText(String.valueOf(comments));
                            tv_totallike.setText(String.valueOf(totallike));
                            name.setText(namev);
                            category.setText(category_name);
                             ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));

                            videoView.setVisibility(View.GONE);
                            Glide.with(ImageDetail.this)
                                    .load(avatar_url)
                                    .into(image);

                            JSONObject jsonObjectUser=responses.getJSONObject("user_detail");
                              String fullnamev=jsonObjectUser.optString("fullname");
                            String avatarr=Config.AVATAR_URL+"250/250/"+jsonObjectUser.optString("avatar");
                            final int uid=jsonObjectUser.optInt("id");
                            fullname.setText(fullnamev);

                            Glide.with(ImageDetail.this)
                                    .load(avatarr)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(imageView_user);
                              imageView_user.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Toast.makeText(getApplicationContext(),"uid:"+uid,Toast.LENGTH_LONG).show();

                                      Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                                      intent.putExtra("uid",String.valueOf(uid));
                                       startActivity(intent);
                                  }
                              });
                             JSONArray group_image=responses.getJSONArray("group_image");
                              for(int i=0;i<group_image.length();i++){
                                   JSONObject image_data=group_image.getJSONObject(i);
                                    int iid=image_data.getInt("id");
                                    String namee=image_data.optString("name");
                                    String imagevv=image_data.optString("image");
                                  String imagev=Config.ALBUM_URL+imagevv;
                                     String udatee=image_data.optString("udate");
                                    String about_usv=image_data.optString("about_us");
                                     int like_unlikei=image_data.optInt("like_unlike");
                                     String like_unlikev=image_data.optString("like_unlike");
                                   int ratingv=image_data.optInt("rating");
                                    int commentsv=image_data.optInt("comments");
                                     int totallikev=image_data.optInt("totallike");
                                  String udateee=image_data.optString("udate");
                                  String category_namee=image_data.optString("category_name");
                                  JSONObject jsonObjectUserr=image_data.getJSONObject("user_detail");

                                  String fullnamee=jsonObjectUser.optString("fullname");
                                  String avataru=Config.AVATAR_URL+"250/250/"+jsonObjectUserr.optString("avatar");
                                  int uidd=jsonObjectUserr.optInt("id");

                                  myData.add(new ImageDetailModel(iid,namee,imagev,about_usv,like_unlikev,ratingv,commentsv,totallikev,uidd,fullnamee,avataru,udateee,category_namee,IMAGE_TYPE));
                              }
                               Log.d("img_video",""+myData);
                             adapter=new Img_Video_Details(getApplicationContext(),myData,ImageDetail.this);
                            recycler_view_review_detail.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            recycler_view_review_detail.setNestedScrollingEnabled(false);
                            recycler_view_review_detail.setAdapter(adapter);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    protected void getVideoDetail(){
        String url="https://www.iampro.co/api/app_service.php?type=get_image_detail&id="+id+"&update_type="+type+"&uid=1&login_id="+uid+"&my_id="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(ImageDetail.this);

        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Prod_detail",response.toString());
                        // Process the JSON
                        try{
                            JSONObject responses=new JSONObject(response);

                            int id =responses.optInt("id");
                            int albemid =responses.optInt("albemid");
                            String namev =responses.optString("name");
                            int categoryv=responses.optInt("category");
                            int albem_type=responses.optInt("albem_type");
                            String imagee=responses.optString("image");
                            String category_name=responses.optString("category_name");
                            //https://www.iampro.co/uploads/album/w/500/45.png
                            String avatar_url=Config.ALBUM_URL+imagee;
                            String udatev=responses.optString("udate");
                            String about_us=responses.optString("about_us");
                            int group_id=responses.optInt("group_id");
                            int is_featured=responses.optInt("is_featured");
                            int status=responses.optInt("status");
                            String is_block=responses.optString("is_block");
                            int total_group_image=responses.optInt("total_group_image");
                            int like_unlike=responses.optInt("like_unlike");
                            Double rating=responses.optDouble("rating");
                            int totallike=responses.optInt("totallike");
                            int comments=responses.optInt("comments");
                            //tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category
                             tv_about_tag.setText("About Video");
                             tv_about_msg.setText(about_us);
                             udate.setText(udatev);
                             tv_comments.setText(String.valueOf(comments));
                            tv_totallike.setText(String.valueOf(totallike));
                            name.setText(namev);
                            category.setText(category_name);
                            ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));

                            /*Glide.with(ImageDetail.this)
                                    .load(avatar_url)
                                    .into(image); */
                            videoView.setVisibility(View.VISIBLE);

                            videoView.setVideoPath(avatar_url);
                            videoView.start();
                             image.setVisibility(View.GONE);

                            JSONObject jsonObjectUser=responses.getJSONObject("user_detail");
                            String fullnamev=jsonObjectUser.optString("fullname");
                            String avatarr=Config.AVATAR_URL+"250/250/"+jsonObjectUser.optString("avatar");
                            int uid=jsonObjectUser.optInt("id");
                            fullname.setText(fullnamev);

                            Glide.with(ImageDetail.this)
                                    .load(avatarr)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(imageView_user);

                            JSONArray group_image=responses.getJSONArray("group_image");
                            for(int i=0;i<group_image.length();i++){
                                JSONObject image_data=group_image.getJSONObject(i);
                                int iid=image_data.getInt("id");
                                String namee=image_data.optString("name");
                                String imagevv=image_data.optString("image");
                                String imagev=Config.ALBUM_URL+imagevv;
                                String udatee=image_data.optString("udate");
                                String about_usv=image_data.optString("about_us");
                                int like_unlikei=image_data.optInt("like_unlike");
                                String like_unlikev=image_data.optString("like_unlike");
                                int ratingv=image_data.optInt("rating");
                                int commentsv=image_data.optInt("comments");
                                int totallikev=image_data.optInt("totallike");
                                String udateee=image_data.optString("udate");
                                String category_namee=image_data.optString("category_name");
                                JSONObject jsonObjectUserr=image_data.getJSONObject("user_detail");

                                String fullnamee=jsonObjectUser.optString("fullname");
                                String avataru=Config.AVATAR_URL+"250/250/"+jsonObjectUserr.optString("avatar");
                                int uidd=jsonObjectUserr.optInt("id");

                                myData.add(new ImageDetailModel(iid,namee,imagev,about_usv,like_unlikev,ratingv,commentsv,totallikev,uidd,fullnamee,avataru,udateee,category_namee,VIDEO_TYPE));
                            }
                            Log.d("img_video",""+myData);
                            adapter=new Img_Video_Details(getApplicationContext(),myData,ImageDetail.this);
                            recycler_view_review_detail.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            recycler_view_review_detail.setNestedScrollingEnabled(false);
                            recycler_view_review_detail.setAdapter(adapter);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onItemClick(ImageDetailModel item) {

    }
}
