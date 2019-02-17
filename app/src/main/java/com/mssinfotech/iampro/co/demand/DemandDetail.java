package com.mssinfotech.iampro.co.demand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.adapter.DemandAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.customphoto.cropoverlay.utils.Utils;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetail;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;

public class DemandDetail extends AppCompatActivity  implements CommentAdapter.ItemListener
{
    public String pid="",uid="";
    TextView tv_name,tv_categories,tv_cost,tv_demanddetails,tv_demand_name,tv_demand_email;

    de.hdodenhof.circleimageview.CircleImageView user_image;
    RecyclerView recycler_view_review_demand;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView expandedImage;
    ArrayList<Review> items=new ArrayList<>();
    CommentAdapter comment_adapter;

    FullscreenVideoView fullscreenVideoView;
    SliderLayout imageSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_detail);
        //items=new ArrayList<>();
        //Set toolbar title
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        expandedImage=collapsingToolbar.findViewById(R.id.expandedImage);
        //collapsingToolbar.setTitle("Product");
        tv_name=findViewById(R.id.tv_name);
        tv_categories=findViewById(R.id.tv_categories);
        imageSlider = findViewById(R.id.imageSlider);
        fullscreenVideoView = findViewById(R.id.fullscreenVideoView);
        tv_cost=findViewById(R.id.tv_cost);
        tv_demanddetails=findViewById(R.id.tv_demanddetails);
        tv_demand_name=findViewById(R.id.tv_demand_name);
        tv_demand_email=findViewById(R.id.tv_demand_email);
        user_image=findViewById(R.id.user_image);

        recycler_view_review_demand=findViewById(R.id.recycler_view_review_demand);

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
        getDemandDetail();
        getDemandReview();
    }
    protected void getDemandDetail(){
       // String url= Config.API_URL+"ajax.php?type=provide_details&id="+pid+"&uid="+uid;
         String url="https://www.iampro.co/api/app_service.php?id="+pid+"&uid="+uid+"&my_id="+uid+"&type=provide_details";
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
                        Log.d("Prod_detail_demand",response.toString());
                        // Process the JSON
                        try{
                            if (response.toString().equalsIgnoreCase("") || response==null){
                                return;
                            }
                            JSONObject responses=new JSONObject(response);

                            // Get the current student (json object) data
                            String name =responses.optString("name");
                            String category =responses.optString("category");
                            String cost ="INR:"+" "+responses.optString("selling_cost")+" Rs";

                            String product_details =responses.optString("detail");
                            String product_provide_name =responses.optString("user_name");
                            String product_provide_email =responses.optString("email");
                            String avatar=responses.optString("avatar");
                            String image=responses.optString("image");
                            String image_path=Config.OTHER_IMAGE_URL+image;

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
                                    .load(image_path)
                                    .apply(Config.options_demand)
                                    .into(expandedImage);

                            Glide.with(DemandDetail.this)
                                    .load(avatar_path)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(user_image);


                            imageSlider.setVisibility(View.VISIBLE);
                            JSONArray other_imagee=responses.getJSONArray("myother_img");
                            // JSONArray other_image = result.getJSONArray("image_array");
                            String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+responses.getString("image");
                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                            //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                            DefaultSliderView sliderView1 = new DefaultSliderView(DemandDetail.this);
                            sliderView1.setImageUrl(ImageHol);
                            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                            //sliderView.setDescription("setDescription " + (i + 1));
                            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(SliderView sliderView) {
                                    //todo
                                    /*
                                     * all full screen view
                                     * */
                                }
                            });

                            //at last add this view in your layout :
                            imageSlider.addSliderView(sliderView1);
                            if(other_imagee.length()>0){
                                for(int i=0; i<other_imagee.length(); i++){
                                    DefaultSliderView sliderView = new DefaultSliderView(DemandDetail.this);
                                    sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_imagee.getString(i));
                                    Log.d("provide_detail",Config.URL_ROOT+"uploads/product/w/500/"+other_imagee.getString(i));
                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    //sliderView.setDescription("setDescription " + (i + 1));
                                    final int finalI = i;
                                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(SliderView sliderView) {
                                            //todo
                                            /*
                                            * all full screen view
                                            * */
                                        }
                                    });

                                    //at last add this view in your layout :
                                    imageSlider.addSliderView(sliderView);
                                }
                            }



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

                        try{
                            if (!items.isEmpty()){
                                items.clear();
                            }

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
                            Log.d("demand_itemss",items+"");
                            comment_adapter=new CommentAdapter(DemandDetail.this,items,DemandDetail.this);
                            recycler_view_review_demand.setLayoutManager(new LinearLayoutManager(DemandDetail.this, LinearLayoutManager.VERTICAL, false));

                            recycler_view_review_demand.setAdapter(comment_adapter);
                        }catch (JSONException e){
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

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    public void sendReview(View view){
         if(PrefManager.isLogin(DemandDetail.this)) {
             // get prompts.xml view
             LayoutInflater li = LayoutInflater.from(DemandDetail.this);
             View promptsView = li.inflate(R.layout.prompts_review, null);

             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                     DemandDetail.this);

             // set prompts.xml to alertdialog builder
             alertDialogBuilder.setView(promptsView);

             final EditText userInput = promptsView
                     .findViewById(R.id.editTextReview);

             // set dialog message
             alertDialogBuilder
                     .setCancelable(false)
                     .setPositiveButton("OK",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     //Toast.makeText(getApplicationContext(), userInput.getText(), Toast.LENGTH_LONG).show();
                                      saveReview(userInput.getText().toString().trim());
                                     getDemandReview();
                                 }
                             })
                     .setNegativeButton("Cancel",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     dialog.cancel();
                                 }
                             });

             // create alert dialog
             AlertDialog alertDialog = alertDialogBuilder.create();

             // show it
             alertDialog.show();
         }
         else{
              Toast.makeText(DemandDetail.this,"First Login and try again...",Toast.LENGTH_LONG).show();
              return;
         }
    }
    public void saveReview(String message){
        String url="https://www.iampro.co/api/app_service.php?type=product_review&data_id="+pid+"&comment="+message+"&id="+uid+"&data_type=demand";
        //id: 693
        //data_type: demand
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(DemandDetail.this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            String msg=response.optString("msg");
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(DemandDetail.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(DemandDetail.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public void chat(View view){
        if(PrefManager.isLogin(DemandDetail.this)) {
            Intent intent = new Intent(DemandDetail.this, MessageActivity.class);
            this.startActivity(intent);
        }
        else {
            Toast.makeText(DemandDetail.this,"First Login and try again...",Toast.LENGTH_LONG).show();
        }
    }
    public void favourite(View view){
        if(PrefManager.isLogin(DemandDetail.this)) {

        }
        else {
            Toast.makeText(DemandDetail.this,"First Login and try again...",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(Review item) {

    }
}
