package com.mssinfotech.iampro.co.demand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.ChatToUser;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
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

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;
import static com.mssinfotech.iampro.co.common.Config.options_avatar;

public class DemandDetailActivity extends AppCompatActivity implements CommentAdapter.ItemListener
{
    public String pid="",uid="";
    TextView tv_name,tv_categories,tv_cost,tv_demanddetails,tv_demand_name,tv_demand_email;

    de.hdodenhof.circleimageview.CircleImageView user_image;
    RecyclerView recycler_view_review_demand;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView expandedImage;
    ArrayList<Review> items=new ArrayList<>();
    CommentAdapter comment_adapter;
    LikeButton favButton;
    FullscreenVideoView fullscreenVideoView;
    SliderLayout imageSlider;
    JSONArray other_imagees;
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
        favButton = findViewById(R.id.favButton);
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
        //uid=getIntent().getExtras().getString("uid");
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(uid));
                function.loadFragment(DemandDetailActivity.this,fragment,args);
            }
        });
        if (PrefManager.isLogin(DemandDetailActivity.this)) {
            favButton.setEnabled(true);
        }
        else {
            favButton.setEnabled(false);
        }
        getDemandDetail();
    }
    protected void getDemandDetail(){
        // String url= Config.API_URL+"ajax.php?type=provide_details&id="+pid+"&uid="+uid;
        String url=Config.API_URL+ "app_service.php?id="+pid+"&uid="+uid+"&my_id="+uid+"&type=provide_details";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(DemandDetailActivity.this);

        // Initialize a new JsonObjectRequest instance
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response
                        //mTextView.setText(response.toString());
                        Log.d("Prod_detail_demand",response.toString()+"\t\t"+pid);
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
                            uid = responses.getString("added_by");
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

                            Glide.with(DemandDetailActivity.this)
                                    .load(image_path)
                                    .apply(Config.options_demand)
                                    .into(expandedImage);

                            Glide.with(DemandDetailActivity.this)
                                    .load(avatar_path)
                                    .apply(Config.options_avatar)
                                    .into(user_image);

                            if (responses.getBoolean("wishlist")){
                                favButton.setLiked(true);
                            }
                            favButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(pid)+"&uid="+uid+"&ptype=provide";
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }
                                @Override
                                public void unLiked(LikeButton likeButton) {
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(pid)+"&uid="+uid+"&ptype=provide";
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }
                            });
                            imageSlider.setVisibility(View.VISIBLE);
                            JSONArray other_imagee=responses.getJSONArray("myother_img");
                            other_imagees=other_imagee;
                            // JSONArray other_image = result.getJSONArray("image_array");
                            String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+responses.getString("image");
                            final String ImageHolInfo = Config.URL_ROOT+"uploads/product/"+responses.getString("image");
                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                            //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                            DefaultSliderView sliderView1 = new DefaultSliderView(DemandDetailActivity.this);
                            sliderView1.setImageUrl(ImageHol);
                            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                            //sliderView.setDescription("setDescription " + (i + 1));
                            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(SliderView sliderView) {
                                    new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHolInfo, null);
                                }
                            });

                            //at last add this view in your layout :
                            imageSlider.addSliderView(sliderView1);
                            if(other_imagee.length()>0){
                                for(int i=0; i<other_imagee.length(); i++){
                                    DefaultSliderView sliderView = new DefaultSliderView(DemandDetailActivity.this);
                                    sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_imagee.getString(i));
                                    final String ImageHolFull = Config.URL_ROOT+"uploads/product/"+other_imagee.getString(i);
                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    //sliderView.setDescription("setDescription " + (i + 1));
                                    final int finalI = i;
                                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(SliderView sliderView) {
                                            //new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHolFull, null);

                                            View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
                                            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                                            // Example: If you have a TextView inside `popup_layout.xml`
                                            final ImageView iview=popupView.findViewById(R.id.expandedImage);
                                            SliderLayout imageSlider=popupView.findViewById(R.id.imageSlider);
                                            //Toolbar toolbar=popupView.findViewById(R.id.toolbar);

                                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                                            //if (other_imagee.length()>1)
                                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                                            //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                                            DefaultSliderView sliderView1 = new DefaultSliderView(DemandDetailActivity.this);
                                            sliderView1.setImageUrl(ImageHolInfo);
                                            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                            //sliderView.setDescription("setDescription " + (i + 1));
                                            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                                @Override
                                                public void onSliderClick(SliderView sliderView) {
                                                    //new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHolFull, null);
                                                }
                                            });

                                            //at last add this view in your layout:
                                            imageSlider.addSliderView(sliderView1);
                                            if(other_imagees.length()>0){
                                                for(int i=0; i<other_imagees.length(); i++){
                                                    DefaultSliderView sliderVieww = new DefaultSliderView(DemandDetailActivity.this);
                                                    try {
                                                        sliderView.setImageUrl(Config.URL_ROOT + "uploads/product/w/500/" + other_imagees.getString(i));
                                                        final String myImage = Config.URL_ROOT + "uploads/product/w/500/" + other_imagees.getString(i);
                                                    }
                                                    catch(JSONException je){
                                                        Toast.makeText(DemandDetailActivity.this,""+je.getMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                                    //sliderView.setDescription("setDescription " + (i + 1));
                                                    final int finalI = i;
                                                    /*sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                                        @Override
                                                        public void onSliderClick(SliderView sliderView) {
                                                            new PhotoFullPopupWindow(ProvideDetailActivity.this, R.layout.popup_photo_full, iview.getRootView(), myImage, null);
                                                        }
                                                    }); */

                                                    //at last add this view in your layout :
                                                    imageSlider.addSliderView(sliderView);
                                                }
                                            }
                                            else{
                                                Glide.with(DemandDetailActivity.this)
                                                        .load(ImageHolInfo)
                                                        .apply(Config.options_avatar)
                                                        .into(iview);
                                            }
                                            // Initialize more widgets from `popup_layout.xml`

                                            // If the PopupWindow should be focusable
                                            popupWindow.setFocusable(true);

                                            // If you need the PopupWindow to dismiss when when touched outside
                                            popupWindow.setBackgroundDrawable(new ColorDrawable());

                                            int location[] = new int[2];

                                            // Get the View's(the one that was clicked in the Fragment) location
                                            favButton.getLocationOnScreen(location);

                                            // Using location, the PopupWindow will be displayed right under anchorView
                                            popupWindow.showAtLocation(favButton, Gravity.NO_GRAVITY, location[0], location[1] + favButton.getHeight());

                                        }
                                    });

                                    //at last add this view in your layout :
                                    imageSlider.addSliderView(sliderView);
                                }
                            }
                            else if (other_imagee.length()==1){
                                imageSlider.setVisibility(View.GONE);
                                expandedImage.setVisibility(View.VISIBLE);
                                Glide.with(DemandDetailActivity.this)
                                        .load(ImageHolInfo)
                                        .apply(Config.options_demand)
                                        .into(expandedImage);
                                expandedImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(),ImageHolInfo, null);
                                    }
                                });
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
    public void chat(View view){
        if(PrefManager.isLogin(DemandDetailActivity.this)) {
            Toast.makeText(DemandDetailActivity.this,"redirect to start chat with "+uid+"...",Toast.LENGTH_LONG).show();
             Intent intent=new Intent(DemandDetailActivity.this, ChatToUser.class);
              intent.putExtra("id",String.valueOf(uid));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
        else {
            Toast.makeText(DemandDetailActivity.this,"First Login and try again...",Toast.LENGTH_LONG).show();
        }
    }
    public void CommentClick(View view){
        Intent intent=new Intent(this, CommentActivity.class);
        intent.putExtra("type","demand");
        intent.putExtra("id",String.valueOf(pid));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    public void onItemClick(Review item) {

    }
}