package com.mssinfotech.iampro.co.provide;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.ChatToUser;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.ChatList;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.product.ProductDetail;
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

public class ProvideDetailActivity extends AppCompatActivity implements CommentAdapter.ItemListener
{
    CollapsingToolbarLayout collapsingToolbar;
    public String pid="",uid="";
    TextView tv_name,tv_categories,tv_cost,tv_provdetails,tv_prod_prov_name,tv_prod_prov_email;

    de.hdodenhof.circleimageview.CircleImageView user_image;
    RecyclerView recycler_view_review_provide;
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
        setContentView(R.layout.activity_provide_detail);
        //Set toolbar title
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
          expandedImage=collapsingToolbar.findViewById(R.id.expandedImage);
        //collapsingToolbar.setTitle("Product");
        favButton = findViewById(R.id.favButton);
        tv_name=findViewById(R.id.tv_name);
        tv_categories=findViewById(R.id.tv_categories);
        tv_cost=findViewById(R.id.tv_cost);
        tv_provdetails=findViewById(R.id.tv_provdetails);
        imageSlider = findViewById(R.id.imageSlider);
        fullscreenVideoView = findViewById(R.id.fullscreenVideoView);
        tv_prod_prov_name=findViewById(R.id.tv_prod_prov_name);
        tv_prod_prov_email=findViewById(R.id.tv_prod_prov_email);
        user_image=findViewById(R.id.user_image);

        recycler_view_review_provide=findViewById(R.id.recycler_view_review_provide);

        pid=getIntent().getExtras().getString("pid");
        //uid=getIntent().getExtras().getString("uid");
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(uid));
                function.loadFragment(ProvideDetailActivity.this,fragment,args);
            }
        });
        getProvideDetail();
        //getProvideReview();
    }
    protected void getProvideDetail(){
        String url= Config.API_URL+"ajax.php?type=provide_details&id="+pid+"&uid="+PrefManager.getLoginDetail(this,"id");
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
                        Log.d("Prod_detail",response.toString()+"\t\t"+pid);
                        // Process the JSON
                        try{
                            final JSONObject responses=new JSONObject(response);

                            // Get the current student (json object) data
                            String name =responses.optString("name");
                            String category =responses.optString("category");
                            String cost ="INR:"+" "+responses.optString("selling_cost")+" Rs";

                            String product_details =responses.optString("detail");
                            String product_provide_name =responses.optString("user_name");
                            String product_provide_email =responses.optString("email");
                            String avatar=responses.optString("avatar");
                            String avatar_path=AVATAR_URL+avatar;
                            uid = responses.getString("added_by");
                            String image=responses.optString("image");
                            String image_path=Config.OTHER_IMAGE_URL+image;

                            String other_image=responses.optString("other_image");

                            String other_image_path=Config.OTHER_IMAGE_URL+other_image;

                            // Display the formatted json data in text view
                            tv_name.setText(name);
                            tv_categories.setText(category);
                            tv_cost.setText(cost);
                            tv_provdetails.setText(product_details);
                            tv_prod_prov_name.setText(product_provide_name);
                            tv_prod_prov_email.setText(product_provide_email);
                            /*Glide.with(ProvideDetailActivity.this)
                                    .load(image_path)
                                    .apply(Config.options_provide)
                                    .into(expandedImage); */
                            Glide.with(ProvideDetailActivity.this)
                                    .load(avatar_path)
                                    .apply(Config.options_demand)
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
                            final String ImageHol = Config.URL_ROOT+"uploads/product/"+responses.getString("image");
                            final String ImageHoly = Config.URL_ROOT+"uploads/product/w/500/"+responses.getString("image");
                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                            //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                            DefaultSliderView sliderView1 = new DefaultSliderView(ProvideDetailActivity.this);
                            sliderView1.setImageUrl(ImageHoly);
                            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                            //sliderView.setDescription("setDescription " + (i + 1));
                            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(SliderView sliderView) {
                                    new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHol, null);
                                    Toast.makeText(getApplicationContext(), "slider1", Toast.LENGTH_SHORT).show();
                                 }
                            });

                            //at last add this view in your layout :
                            imageSlider.addSliderView(sliderView1);
                            if(other_imagee.length()>1){
                                for(int i=0; i<other_imagee.length(); i++){
                                    DefaultSliderView sliderView = new DefaultSliderView(ProvideDetailActivity.this);
                                    sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_imagee.getString(i));
                                    final String myImage = (Config.URL_ROOT+"uploads/product/"+other_imagee.getString(i));
                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(SliderView sliderView) {
                                           // Toast.makeText(getApplicationContext(),"sliderview",Toast.LENGTH_LONG).show();
                                            //new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), myImage, null);

                                            View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
                                            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                                            // Example: If you have a TextView inside `popup_layout.xml`
                                            final ImageView iview=popupView.findViewById(R.id.expandedImage);
                                            SliderLayout imageSlider=popupView.findViewById(R.id.imageSlider);
                                            Toolbar toolbar=popupView.findViewById(R.id.toolbar);

                                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                                            //if (other_imagee.length()>1)
                                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                                            //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                                            DefaultSliderView sliderView1 = new DefaultSliderView(ProvideDetailActivity.this);
                                            sliderView1.setImageUrl(ImageHoly);
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
                                                    DefaultSliderView sliderVieww = new DefaultSliderView(ProvideDetailActivity.this);
                                                    try {
                                                        sliderView.setImageUrl(Config.URL_ROOT + "uploads/product/w/500/" + other_imagees.getString(i));
                                                        final String myImage = Config.URL_ROOT + "uploads/product/w/500/" + other_imagees.getString(i);
                                                    }
                                                    catch(JSONException je){
                                                        Toast.makeText(ProvideDetailActivity.this,""+je.getMessage(),Toast.LENGTH_LONG).show();
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
                                                Glide.with(ProvideDetailActivity.this)
                                                        .load(ImageHoly)
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
                            else if(other_imagee.length()==1){
                                imageSlider.setVisibility(View.GONE);
                                expandedImage.setVisibility(View.VISIBLE);
                                Glide.with(ProvideDetailActivity.this)
                                        .load(ImageHoly)
                                        .apply(Config.options_avatar)
                                        .into(expandedImage);

                                expandedImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(),ImageHoly, null);
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
        if(PrefManager.isLogin(ProvideDetailActivity.this)) {
            Toast.makeText(ProvideDetailActivity.this,"redirect to start chat with "+uid+"...",Toast.LENGTH_LONG).show();
             Intent intent=new Intent(ProvideDetailActivity.this,ChatToUser.class);
              intent.putExtra("id",String.valueOf(uid));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
        }
        else {
            Toast.makeText(ProvideDetailActivity.this,"First Login and try again...",Toast.LENGTH_LONG).show();
        }
    }
    public void CommentClick(View view){
        Intent intent=new Intent(this, CommentActivity.class);
        intent.putExtra("type","provide");
        intent.putExtra("id",String.valueOf(pid));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
    @Override
    public void onItemClick(Review item) {

    }
}