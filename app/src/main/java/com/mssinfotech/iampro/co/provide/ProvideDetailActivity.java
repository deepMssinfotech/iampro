package com.mssinfotech.iampro.co.provide;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import org.json.JSONObject;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;

public class ProvideDetailActivity extends AppCompatActivity
{
    CollapsingToolbarLayout collapsingToolbar;
    public static String pid="",uid="";
    TextView tv_name,tv_categories,tv_cost,tv_provdetails,tv_prod_prov_name,tv_prod_prov_email;

    de.hdodenhof.circleimageview.CircleImageView user_image;
    RecyclerView recycler_view_review_provide;
    ImageView expandedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_detail);

        //Set toolbar title
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
          expandedImage=collapsingToolbar.findViewById(R.id.expandedImage);
        //collapsingToolbar.setTitle("Product");

        tv_name=findViewById(R.id.tv_name);
        tv_categories=findViewById(R.id.tv_categories);
        tv_cost=findViewById(R.id.tv_cost);
        tv_provdetails=findViewById(R.id.tv_provdetails);
        tv_prod_prov_name=findViewById(R.id.tv_prod_prov_name);
        tv_prod_prov_email=findViewById(R.id.tv_prod_prov_email);
        user_image=findViewById(R.id.user_image);

        recycler_view_review_provide=findViewById(R.id.recycler_view_review_provide);

        pid=getIntent().getExtras().getString("pid");
        uid=getIntent().getExtras().getString("uid");
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProvideDetailActivity.this,"uid:"+uid,Toast.LENGTH_LONG).show();

                Intent intent=new Intent(ProvideDetailActivity.this, ProfileActivity.class);
                intent.putExtra("uid",String.valueOf(uid));
                ProvideDetailActivity.this.startActivity(intent);
            }
        });
        getProvideDetail();
    }
    protected void getProvideDetail(){
        String url= Config.API_URL+"ajax.php?type=provide_details&id="+pid+"&uid="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(ProvideDetailActivity.this);

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
                            String avatar_path=AVATAR_URL+avatar;

                            String other_image=responses.optString("other_image");

                            String other_image_path=Config.OTHER_IMAGE_URL+other_image;

                            // Display the formatted json data in text view
                            tv_name.setText(name);
                            tv_categories.setText(category);
                            tv_cost.setText(cost);
                            tv_provdetails.setText(product_details);
                            tv_prod_prov_name.setText(product_provide_name);
                            tv_prod_prov_email.setText(product_provide_email);
                            Glide.with(ProvideDetailActivity.this)
                                    .load(other_image_path)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(expandedImage);
                            Glide.with(ProvideDetailActivity.this)
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
}