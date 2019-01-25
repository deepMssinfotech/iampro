package com.mssinfotech.iampro.co.demand;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;

public class DemandDetail extends AppCompatActivity
{
    public static String pid="",uid="";
    TextView tv_name,tv_categories,tv_cost,tv_demanddetails,tv_demand_name,tv_demand_email;

    de.hdodenhof.circleimageview.CircleImageView user_image;
    RecyclerView recycler_view_review_demand;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView expandedImage;
    List<Review> items;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_detail);

        //Set toolbar title
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        expandedImage=collapsingToolbar.findViewById(R.id.expandedImage);
        //collapsingToolbar.setTitle("Product");
        tv_name=findViewById(R.id.tv_name);
        tv_categories=findViewById(R.id.tv_categories);
        tv_cost=findViewById(R.id.tv_cost);
        tv_demanddetails=findViewById(R.id.tv_demanddetails);
        tv_demand_name=findViewById(R.id.tv_demand_name);
        tv_demand_email=findViewById(R.id.tv_demand_email);
        user_image=findViewById(R.id.user_image);

        recycler_view_review_demand=findViewById(R.id.recycler_view_review_provide);

        pid=getIntent().getExtras().getString("pid");
        uid=getIntent().getExtras().getString("uid");
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DemandDetail.this,"uid:"+uid,Toast.LENGTH_LONG).show();

                Intent intent=new Intent(DemandDetail.this, ProfileActivity.class);
                intent.putExtra("uid",String.valueOf(uid));
                  DemandDetail.this.startActivity(intent);
            }
        });
        getProvideDetail();
    }
    protected void getProvideDetail(){
        String url= Config.API_URL+"ajax.php?type=provide_details&id="+pid+"&uid="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(DemandDetail.this);

        // Initialize a new JsonObjectRequest instance
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response
                        //mTextView.setText(response.toString());
                        Log.d("Prod_detail",response.toString());
                        // Process the JSON
                        try{
                            JSONObject responses=new JSONObject(response);

                            // Get the current student (json object) data
                            String name =responses.optString("name");
                            String category =responses.optString("category");
                            String cost ="INR:"+" "+responses.optString("selling_cost")+" Rs";

                            String product_details =responses.optString("detail");
                            String product_provide_name =responses.optString("user_name");
                            String product_provide_email =responses.optString("email");
                            String avatar=responses.optString("avatar");

                            String other_image=responses.optString("other_image");

                            String other_image_path=Config.OTHER_IMAGE_URL+other_image;

                            String avatar_path=AVATAR_URL+avatar;
                            // Display the formatted json data in text view
                            tv_name.setText(name);
                            tv_categories.setText(category);
                            tv_cost.setText(cost);
                            tv_demanddetails.setText(product_details);
                            tv_demand_name.setText(product_provide_name);
                            tv_demand_email.setText(product_provide_email);

                              //expandedImage

                            Glide.with(DemandDetail.this)
                                    .load(other_image_path)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(expandedImage);

                            Glide.with(DemandDetail.this)
                                    .load(avatar_path)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(user_image);
                            //user_image
                            // mTextView.append("\n\n");
                        }catch (Exception e){
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

    public void getDemandReview(){
         //https://www.iampro.co/api/
     String url=Config.API_URL+"ajax.php?type=getProductReview&pcid="+pid+"&id="+pid;

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                         items=new ArrayList<>();
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                // Get the current student (json object) data
                                String id = student.optString("id");
                                String added_by = student.optString("added_by");
                                String pid = student.optString("pid");
                                String pcid = student.optString("pcid");
                                String comment = student.optString("comment");
                                String fullname = student.optString("fullname");
                                String email = student.optString("email");
                                 String user_img=student.optString("user_img");
                                  String rdate=student.optString("rdate");
                                    items.add(new Review(fullname,email,comment,id,pcid,user_img,rdate,added_by,pid));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
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

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    }
