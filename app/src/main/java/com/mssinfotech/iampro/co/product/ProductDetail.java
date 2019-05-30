package com.mssinfotech.iampro.co.product;

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
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.Review;
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

public class ProductDetail extends AppCompatActivity implements CommentAdapter.ItemListener {
    CollapsingToolbarLayout collapsingToolbar;
    public String pid = "", uid = "";
    TextView tv_name, tv_categories, tv_cost, tv_proddetails, tv_prod_prov_name, tv_prod_prov_email;

    de.hdodenhof.circleimageview.CircleImageView user_image;
    RecyclerView recycler_view_review_product;
    ImageView expandedImage;
    ArrayList<Review> items = new ArrayList<>();
    CommentAdapter comment_adapter;

    FullscreenVideoView fullscreenVideoView;
    SliderLayout imageSlider;
    String product_id,product_price;
    JSONArray other_imagees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        //Set toolbar title
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        expandedImage = collapsingToolbar.findViewById(R.id.expandedImage);
        //collapsingToolbar.setTitle("Product");
        tv_name = findViewById(R.id.tv_name);
        tv_categories = findViewById(R.id.tv_categories);
        tv_cost = findViewById(R.id.tv_cost);
        tv_proddetails = findViewById(R.id.tv_proddetails);
        imageSlider = findViewById(R.id.imageSlider);
        fullscreenVideoView = findViewById(R.id.fullscreenVideoView);
        tv_prod_prov_name = findViewById(R.id.tv_prod_prov_name);
        tv_prod_prov_email = findViewById(R.id.tv_prod_prov_email);
        user_image = findViewById(R.id.user_image);

        recycler_view_review_product = findViewById(R.id.recycler_view_review_product);

        pid = getIntent().getExtras().getString("pid");
        //uid = getIntent().getExtras().getString("uid");
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(uid));
                function.loadFragment(ProductDetail.this,fragment,args);*/

                Intent intent1=new Intent(ProductDetail.this,ProfileActivity.class);
                intent1.putExtra("uid",String.valueOf(uid));
                startActivity(intent1);
            }
        });
        getProductDetail();
        //getProductReview();
    }

    protected void getProductDetail() {
        String url = Config.API_URL + "ajax.php?type=product_details&id=" + pid + "&uid=" + uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetail.this);

        // Initialize a new JsonObjectRequest instance
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response
                        //mTextView.setText(response.toString());
                        Log.d("Prod_detail", response.toString());
                        // Process the JSON
                        try {
                            JSONObject responses = new JSONObject(response);

                            // Get the current student (json object) data
                            String name = responses.optString("name");
                            String category = responses.optString("category");
                            String cost = "INR:" + " " + responses.optString("selling_cost") + " Rs";
                            product_price = responses.optString("selling_cost");
                            String product_details = responses.optString("detail");
                            String product_provide_name = responses.optString("user_name");
                            //JSONArray myother_img=responses.getJSONArray("myother_img");
                            uid = responses.getString("added_by");
                            String product_provide_email = responses.optString("email");
                            String avatar = responses.optString("avatar");
                            String avatar_path = AVATAR_URL + avatar;
                            String image = responses.optString("image");
                            String image_path = Config.OTHER_IMAGE_URL + image;
                            String other_image = responses.optString("other_image");

                            String other_image_path = Config.OTHER_IMAGE_URL + other_image;

                            // Display the formatted json data in text view
                            tv_name.setText(name);
                            tv_categories.setText(category);
                            tv_cost.setText(cost);
                            tv_proddetails.setText(product_details);
                            tv_prod_prov_name.setText(product_provide_name);
                            tv_prod_prov_email.setText(product_provide_email);
                            Glide.with(ProductDetail.this)
                                    .load(image_path)
                                    .apply(Config.options_product)
                                    .into(expandedImage);

                            Glide.with(ProductDetail.this)
                                    .load(avatar_path)
                                    .apply(Config.options_avatar)
                                    .into(user_image);

                            imageSlider.setVisibility(View.VISIBLE);
                            JSONArray other_imagee=responses.getJSONArray("myother_img");
                            other_imagees=other_imagee;
                           // JSONArray other_image = result.getJSONArray("image_array");
                            String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+responses.getString("image");
                            final String ImageHolFull = Config.URL_ROOT+"uploads/product/"+responses.getString("image");
                             //if (other_imagee.length()>1)
                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            //if (other_imagee.length()>1)
                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                            //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                            DefaultSliderView sliderView1 = new DefaultSliderView(ProductDetail.this);
                            sliderView1.setImageUrl(ImageHol);
                            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                            //sliderView.setDescription("setDescription " + (i + 1));
                            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(SliderView sliderView) {
                                    new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHolFull, null);
                                }
                            });

                            //at last add this view in your layout :
                            imageSlider.addSliderView(sliderView1);
                            if(other_imagee.length()>1){
                                for(int i=0; i<other_imagee.length(); i++){
                                    DefaultSliderView sliderView = new DefaultSliderView(ProductDetail.this);
                                    sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_imagee.getString(i));
                                    final String myImage = Config.URL_ROOT+"uploads/product/"+other_imagee.getString(i);
                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    //sliderView.setDescription("setDescription " + (i + 1));
                                    final int finalI = i;
                                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(SliderView sliderView) {

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
                                            DefaultSliderView sliderView1 = new DefaultSliderView(ProductDetail.this);
                                            sliderView1.setImageUrl(ImageHolFull);
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
                                                    DefaultSliderView sliderVieww = new DefaultSliderView(ProductDetail.this);
                                                    try {
                                                        sliderView.setImageUrl(Config.URL_ROOT + "uploads/product/w/500/" + other_imagees.getString(i));
                                                        final String myImage = Config.URL_ROOT + "uploads/product/w/500/" + other_imagees.getString(i);
                                                    }
                                                    catch(JSONException je){
                                                        Toast.makeText(ProductDetail.this,""+je.getMessage(),Toast.LENGTH_LONG).show();
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
                                                Glide.with(ProductDetail.this)
                                                        .load(ImageHolFull)
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
                                            user_image.getLocationOnScreen(location);

                                            // Using location, the PopupWindow will be displayed right under anchorView
                                            popupWindow.showAtLocation(user_image, Gravity.NO_GRAVITY, location[0], location[1] + user_image.getHeight());



                                        }
                                    });

                                    //at last add this view in your layout :
                                    imageSlider.addSliderView(sliderView);
                                }
                            }
                           else if(other_imagee.length()==1) {
                                imageSlider.setVisibility(View.GONE);
                                expandedImage.setVisibility(View.VISIBLE);
                                Glide.with(ProductDetail.this)
                                        .load(ImageHolFull)
                                        .apply(Config.options_product)
                                        .into(expandedImage);
                                  expandedImage.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(),ImageHolFull, null);
                                      }
                                  });
                            }


                        } catch (Exception e)  {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    public void getProductReview() {
        String url = Config.API_URL + "ajax.php?type=getProductReview&pid=" + pid;
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

                        try {
                            if (!items.isEmpty()) {
                                items.clear();
                            }
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
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
                                String user_img = student.optString("user_img");
                                String rdate = student.optString("rdate");
                                items.add(new Review(fullname, email, comment, id, pcid, user_img, rdate, added_by, pid));
                            }
                            Log.d("demand_itemss", items + "");
                            comment_adapter = new CommentAdapter(ProductDetail.this, items, ProductDetail.this);
                            recycler_view_review_product.setLayoutManager(new LinearLayoutManager(ProductDetail.this, LinearLayoutManager.VERTICAL, false));

                            recycler_view_review_product.setAdapter(comment_adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
    public void CommentClick(View view){
        Intent intent=new Intent(this, CommentActivity.class);
        intent.putExtra("type","product");
        intent.putExtra("id",String.valueOf(pid));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
    public void MyAddToCart(View view){
        if (PrefManager.isLogin(ProductDetail.this)) {
            function.addtocart(getApplicationContext(), pid, "1", product_price);
            Toast.makeText(ProductDetail.this, "Product has been added to cart...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ProductDetail.this, "First Login and try again...", Toast.LENGTH_LONG).show();
        }
    }
    public void buyNow(View view) {
        if (PrefManager.isLogin(ProductDetail.this)) {
            function.addtocart(getApplicationContext(), pid, "1", product_price);
            CartActivity fragment = new CartActivity();
            function.loadFragment(ProductDetail.this,fragment,null);
        } else {
            Toast.makeText(ProductDetail.this, "First Login and try again...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(Review item) {

    }
}