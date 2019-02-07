package com.mssinfotech.iampro.co.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.mssinfotech.iampro.co.NotificationActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.adapter.MyProductAdapter;
import com.mssinfotech.iampro.co.adapter.ProductAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.data.MessageItem;
import com.mssinfotech.iampro.co.data.MyProductItem;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.tab.ProductFragment;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProductActivity extends AppCompatActivity implements MyProductAdapter.ItemListener {
    private List<MyProductItem> MyProductItemList;

    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username;
    private String uid="";
    RecyclerView recyclerView;
    MyProductAdapter mAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    boolean isLoading = false;
    Intent intent;
    ArrayList<MyProductModel> item = new ArrayList<>();
    MyProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_product));
        intent = getIntent();
        String id = intent.getStringExtra("uid");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        uid= PrefManager.getLoginDetail(this,"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");

            username.setText("My Product");
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            PrefManager.updateUserData(this,null);
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
        /*FloatingActionButton fv=findViewById(R.id.fab);
        fv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_signup = new Intent(MyProductActivity.this,AddProductActivity.class);
                MyProductActivity.this.startActivity(i_signup);
            }
        });*/
        recyclerView = findViewById(R.id.recyclerView);
        //initAdapter();
        initScrollListener();
        getProduct();
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
                            String avatar=Config.AVATAR_URL+"250/250/"+result.getString("avatar");
                            String background=Config.AVATAR_URL+"h/250/"+result.getString("banner_image");
                            username = findViewById(R.id.username);
                            userimage = findViewById(R.id.userimage);
                            userbackgroud = findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Products");
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
        Intent i_signup = new Intent(MyProductActivity.this,AddProductActivity.class);
        MyProductActivity.this.startActivity(i_signup);
    }
    private void initAdapter() {

        //mAdapter = new MyProductAdapter(rowsArrayList);
        //recyclerView.setAdapter(mAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        prepareCart();
                        isLoading = true;
                    }
                }
            }
        });


    }
    public void prepareCart() {
        String uid=PrefManager.getLoginDetail(this,"id");
        String MY_PRODUCT_URL = Config.API_URL+"app_service.php?type=getall_product&added_by="+uid+"&my_id="+uid+"&search_type=PRODUCT";
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,MY_PRODUCT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        parseJsonFeed(response);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Config.ResponceResult = error.getMessage();
                Log.d(Config.TAG,"error : "+error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void parseJsonFeed(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray heroArray = obj.getJSONArray("list");
            if(heroArray.length()>0) {
                for (int i = 0; i < heroArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) heroArray.get(i);
                    MyProductItem item = new MyProductItem();
                    String user_image = Config.AVATAR_URL + "80/80/" + feedObj.getString("avatar");
                    String product_image = Config.URL_ROOT + "uploads/products/80/80/" + feedObj.getString("image");
                    item.setId(feedObj.getInt("id"));
                    item.setAvatar(user_image);
                    item.setImage(product_image);
                    item.setAverage_rating(feedObj.getString("average_rating"));
                    item.setFullname(feedObj.getString("fullname"));
                    item.setLikes(feedObj.getString("like"));
                    item.setPurchese_cost(feedObj.getString("purchese_cost"));
                    item.setSelling_cost(feedObj.getString("selling_cost("));
                    MyProductItemList.add(item);
                }
            }else{
                ImageView no_rodr = findViewById(R.id.no_record_found);
                no_rodr.setVisibility(View.VISIBLE);
            }
            // notify data changes to list adapater
        } catch (JSONException e) {
            Log.d(Config.TAG,"printStackTrace 167" + e.getMessage() + "  Error Message");
            e.printStackTrace();
        }
    }

    public void getProduct(){
         String url=Config.API_URL+"app_service.php?type=getall_product&added_by="+uid+"&my_id="+uid+"&search_type=PRODUCT";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Product");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                int id=student.getInt("id");
                                String idv=String.valueOf(id);
                                int added_by=student.getInt("added_by");

                                int scost=student.getInt("selling_cost");
                                int pcost=student.getInt("purchese_cost");

                                String name = student.getString("name");
                                String categoryv=student.getString("category");
                                //tv_category.setText(categoryv);

                                String imagev=student.getString("image");
                                String image= Config.URL_ROOT + "uploads/product/" +imagev;
                                String udate=student.getString("udate");
                                int totallike=Integer.parseInt(student.getString("likes"));
                                int comments=student.getInt("comments");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);

                                // String daysago=student.getString("ago");

                                String rating=String.valueOf(student.getInt("average_rating"));
                                float ratingv=Float.parseFloat(rating);

                                JSONObject userDetail=student.getJSONObject("user_name");
                                int uid=userDetail.getInt("id");
                                String fname=userDetail.getString("fname");
                                 String lname=userDetail.getString("lname");
                                String fullname=fname+"\t"+lname;
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                // singleItem.add(new SingleItemModel(name,image,udate));
                                //allSampleData.add(new DataModel(name,image,udate,categoryv));
                                item.add(new MyProductModel(name,image,udate,categoryv,totallike,comments,scost,pcost,ratingv,uid,fullname,avatar,idv));
                            }
                            Log.d("bdm",singleItem.toString());
                           // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav",item.toString());
                            adapter = new MyProductAdapter(MyProductActivity.this,item,MyProductActivity.this);

                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);

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
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
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
    public void onItemClick(MyProductModel items) {
       Toast.makeText(getApplicationContext(),""+items.getPid(),Toast.LENGTH_LONG).show();

    }
}
