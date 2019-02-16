package com.mssinfotech.iampro.co.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
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
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        tv_prod_prov_name = findViewById(R.id.tv_prod_prov_name);
        tv_prod_prov_email = findViewById(R.id.tv_prod_prov_email);
        user_image = findViewById(R.id.user_image);

        recycler_view_review_product = findViewById(R.id.recycler_view_review_product);

        pid = getIntent().getExtras().getString("pid");
        uid = getIntent().getExtras().getString("uid");
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetail.this, "uid:" + uid, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ProductDetail.this, ProfileActivity.class);
                intent.putExtra("uid", String.valueOf(uid));
                ProductDetail.this.startActivity(intent);
            }
        });
        getProductDetail();
        getProductReview();
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

                            String product_details = responses.optString("detail");
                            String product_provide_name = responses.optString("user_name");
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
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(user_image);
                            //user_image

                            // mTextView.append("\n\n");

                        } catch (Exception e) {
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

    public void sendReview(View view) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(ProductDetail.this);
        View promptsView = li.inflate(R.layout.prompts_review, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProductDetail.this);

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
                                // get user input and set it to result
                                // edit text
                                // result.setText(userInput.getText());
                                Toast.makeText(getApplicationContext(), userInput.getText(), Toast.LENGTH_LONG).show();
                                saveReview(userInput.getText().toString().trim());
                                getProductReview();
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

    public void saveReview(String message) {
        String url = "https://www.iampro.co/api/app_service.php?type=product_review&data_id=" + pid + "&comment=" + message + "&id=" + uid + "&data_type=product";
        //id: 693
        //data_type: demand
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetail.this);

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
                        try {
                            String msg = response.optString("msg");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDetail.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(ProductDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    public void buyNow(View view) {
        if (PrefManager.isLogin(ProductDetail.this)) {
            Intent intent = new Intent(ProductDetail.this, CartActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(ProductDetail.this, "First Login and try again...", Toast.LENGTH_LONG).show();
        }

    }

    public void addToCart(View view) {
        if (PrefManager.isLogin(ProductDetail.this)) {
            Intent intent = new Intent(ProductDetail.this, CartActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(ProductDetail.this, "First Login and try again...", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onItemClick(Review item) {

    }
}