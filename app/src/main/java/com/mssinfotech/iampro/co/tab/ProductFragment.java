package com.mssinfotech.iampro.co.tab;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.MyProductAdapter;
import com.mssinfotech.iampro.co.adapter.MyVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.ProductAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.common.SlidingImage_Adapter;
import com.mssinfotech.iampro.co.data.ImageModel;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class ProductFragment extends Fragment implements ProductAdapter.ItemListener,MyProductAdapter.ItemListener{
    ArrayList<DataModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view,recycler_view_load_more;
    RecyclerViewAdapter adapter;
    ProductAdapter adapter_product;
    int uid;
    Button btn_load_more;
    ArrayList<MyImageModel> item = new ArrayList<>();
    //MyProductAdapter adapterr;
     MyImageVideoDataAdapter adapterr;
    TreeMap<String,String> item_name=new TreeMap<>();
     ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();
      ImageView lproduct_iv;
    ImageView no_rodr;
    View views;
    //sliderr
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<com.mssinfotech.iampro.co.data.ImageModel> imageModelArrayList;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public  void  likeProduct(){
        String url=Config.API_URL+ "app_service.php?type=like_me&id=5&uid=693&ptype=product";
        Toast.makeText(getContext(),"Frm fragment",Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product, container, false);
        //oolbar =view.findViewById(R.id.toolbar);
        views=view;

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //createDummyData();
        if (PrefManager.isLogin(getContext())) {
            String id = PrefManager.getLoginDetail(getContext(), "id");
            uid = Integer.parseInt(id);
        }
        getProduct();
        my_recycler_view =view.findViewById(R.id.my_recycler_view);
        recycler_view_load_more=view.findViewById(R.id.recycler_view_load_more);
         no_rodr =view.findViewById(R.id.no_record_found);
        btn_load_more=view.findViewById(R.id.btn_load_more);
        imageModelArrayList=new ArrayList<>();
          lproduct_iv=view.findViewById(R.id.lproduct_iv);
        //lproduct_iv.setBackground(getContext().getResources().getDrawable(R.drawable.latestproduct));
        lproduct_iv.setVisibility(View.VISIBLE);
        lproduct_iv.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.latestproduct));
        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProductmore();
                //recycler_view_load_more.setVisibility(View.VISIBLE);
                getAllAlbum();
                //Toast.makeText(getContext(),"loadmore",Toast.LENGTH_LONG).show();
                btn_load_more.setVisibility(View.GONE);
            }
        });
        getTopSlider();
    }
    private void init() {

        mPager = views.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getContext(),imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)views.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void getTopSlider(){
        final String url=Config.API_URL+ "index.php?type=get_slider&name=TOP_SLIDER";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // pDialog.dismiss();
                        Log.d("responsef",response.toString());
                        if (!imageModelArrayList.isEmpty()){
                            imageModelArrayList.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String id = student.optString("id");
                                String heading = student.optString("heading");

                                String slider_type=student.optString("slider_type");
                                //id ,heading,slider_type,link,image,slider_image,status,language,lorder,no,index,mindex
                                String link=student.optString("link");
                                String imagev=student.optString("image");
                                String slider_image=student.optString("slider_image");
                                String status=student.optString("status");
                                String language=student.optString("language");
                                String lorder=student.optString("lorder");
                                int no=student.optInt("no");
                                int index=student.optInt("index");
                                int mindex=student.optInt("mindex");

                                String image= Config.URL_ROOT+"uploads/album/300/250/"+imagev;
                                imageModelArrayList.add(new ImageModel(id,heading,slider_type,link,imagev,slider_image,status,language,lorder,no,index,String.valueOf(mindex)));
                                //singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"image"));
                            }
                            init();
                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //pDialog.dismiss();
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
    public void getAllAlbum(){
        //String url=Config.API_URL+ "app_service.php?type=getAlbemsListt&search_type=video&uid="+uid;
        String url=Config.API_URL+ "app_service.php?type=get_category&name=PRODUCT&uid="+uid;
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());

        final Dialog pDialog = new Dialog(this.getContext());
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        if(!item_name.isEmpty()){
                            item_name.clear();
                        }
                        if(!item.isEmpty()){
                            item.clear();
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject student1 = response.getJSONObject(i);
                                int id=student1.optInt("id");
                                String name=student1.optString("name");
                                int product_count=student1.optInt("product_count");
                                //item_name.add(name1);
                                //item_name.put(name1,album_name)
                                if(product_count>0) {
                                    item_name.put(name, String.valueOf(id));
                                    //Toast.makeText(getContext(),""+product_count,Toast.LENGTH_LONG).show();
                                }
                                else{

                                }
                            }
                            Log.d("allsampledataname",item_name.toString());
                            for (String data:item_name.keySet()){
                                //getVideo(data);
                                getProductMores(data);
                                Log.d("Keyset",""+data);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                        pDialog.dismiss();
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getProduct(){
        //int uid= Integer.parseInt(PrefManager.getLoginDetail(getContext(),"id"));
        final String url =Config.API_URL+"app_service.php?type=all_product&uid="+uid+"&name=product&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final Dialog pDialog = new Dialog(this.getContext());
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        if (response.length() > 0) {
                            no_rodr.setVisibility(View.GONE);

                            Log.d("responsef", response.toString());
                            SectionDataModel dm = new SectionDataModel();
                            dm.setHeaderTitle("Product");
                            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                            if (!singleItem.isEmpty()) {
                                singleItem.clear();
                            }
                            if (!allSampleData.isEmpty()) {
                                allSampleData.clear();
                            }
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    // Get current json object
                                    JSONObject student = response.getJSONObject(i);

                                    int id = student.getInt("id");
                                    String idv = String.valueOf(id);
                                    int added_by = student.getInt("added_by");

                                    int scost = student.getInt("selling_cost");
                                    int pcost = student.getInt("purchese_cost");

                                    String name = student.getString("name");
                                    String categoryv = student.getString("category");
                                    String imagev = student.getString("image");
                                    String image = Config.URL_ROOT + "uploads/product/300/250/" + imagev;
                                    String udate = student.getString("udate");
                                    int totallike = Integer.parseInt(student.getString("totallike"));
                                    int comments = student.getInt("comments");
                                    Log.d("pdata", "" + name + "" + categoryv + "" + image + "" + udate);

                                    // String daysago=student.getString("ago");

                                    String rating = student.getString("rating");
                                    float ratingv = Float.parseFloat(rating);

                                    JSONObject userDetail = student.getJSONObject("user_detail");
                                    int uid = userDetail.getInt("id");
                                    String fullname = userDetail.getString("fullname");
                                    String avatar = Config.AVATAR_URL + "250/250/" + userDetail.getString("avatar");

                                    //SectionDataModel dm = new SectionDataModel();
                                    //dm.setHeaderTitle("Section " + i);
                                    //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                    // singleItem.add(new SingleItemModel(name,image,udate));
                                    //allSampleData.add(new DataModel(name,image,udate,categoryv));
                                    int isliked = student.getInt("like_unlike");
                                    allSampleData.add(new DataModel(name, image, udate, categoryv, totallike, isliked, comments, scost, pcost, ratingv, uid, fullname, avatar, idv, "product"));
                                }
                                Log.d("bdm", singleItem.toString());
                                dm.setAllItemsInSection(singleItem);
                                Log.d("adm", singleItem.toString());
                                Log.d("dmm", dm.toString());
                                //allSampleData.add(dm);
                                Log.d("allsampledatav", allSampleData.toString());
                            /*
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter);
                             */
                                //adapter = new RecyclerViewAdapter(getContext(), allSampleData,ProductFragment.this);
                                //adapter_product
                                adapter_product = new ProductAdapter(getContext(), allSampleData, ProductFragment.this);
                                my_recycler_view.setAdapter(adapter_product);

                                GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                                my_recycler_view.setLayoutManager(manager);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("catch_f", "" + e.getMessage());
                                pDialog.dismiss();
                                no_rodr.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            no_rodr.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                         pDialog.dismiss();
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    @Override
    public void onItemClick(DataModel item) {
        //Toast.makeText(getContext(), item.getName() + " is clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(MyProductModel item) {

    }
    public void getProductMores(final String cname){
        final Dialog pDialog = new Dialog(this.getContext());
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        String url=null;
         try {
             String query = URLEncoder.encode(cname, "utf-8");

        //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
        // String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
          url=Config.API_URL+ "app_service.php?type=search_all_items&search_type=PRODUCT&category="+query+"&search_data=&uid="+uid+"&my_id="+uid;
         }
        catch (UnsupportedEncodingException e){
             e.printStackTrace();
            url=Config.API_URL+ "app_service.php?type=search_all_items&search_type=PRODUCT&category="+cname+"&search_data=&uid="+uid+"&my_id="+uid;

        }
         // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(cname);
                        dm.setAlbemId(item_name.get(cname));
                        dm.setMore("loadmore");
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        //ArrayList<MyProductModel> item = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object

                                JSONObject student = response.getJSONObject(i);
                                String category1=student.getString("category_type");
                                String idd=student.optString("id");
                                //String added_byy=student.optString("added_by");
                                String name1=student.optString("name");
                                //String atype=student.optString("atype");
                                //tv_category.setText(name1);
                                //tv_category.setVisibility(View.GONE);
                                JSONArray jsonArrayPics=student.getJSONArray("pro_detail");
                                Log.d("picssss",jsonArrayPics.toString());

                                for (int j=0;j<jsonArrayPics.length();j++){
                                    JSONObject pics=jsonArrayPics.getJSONObject(j);
                                    int id=pics.optInt("id");

                                    int albemid=pics.optInt("albemid");
                                    String name=pics.optString("name");
                                    String category=pics.optString("category");
                                    int albem_type=pics.optInt("albem_type");

                                    String image=pics.optString("image");
                                    String udate=pics.optString("udate");
                                    String about_us=pics.optString("about_us");

                                    int group_id=pics.optInt("group_id");

                                    int is_featured=pics.optInt("is_featured");
                                    //status
                                    int status=pics.optInt("status");
                                    int totallike=pics.optInt("totallike");
                                    int comments=pics.optInt("comments");
                                    int like_unlike=pics.optInt("like_unlike");
                                    //String rating=pics.optString("rating");
                                    String is_block=pics.optString("is_block");

                                    JSONObject userDetail=pics.getJSONObject("user_detail");
                                    int user_id=userDetail.optInt("id");

                                    String username=userDetail.optString("username");
                                    String avatar=userDetail.optString("avatar");
                                    String email=userDetail.optString("email");
                                    String mobile=userDetail.optString("mobile");
                                    String about_me=userDetail.optString("about_me");
                                    String country=userDetail.optString("country");
                                    String state=userDetail.optString("state");
                                    String city=userDetail.optString("city");
                                    String gender=userDetail.optString("gender");
                                    String dob=userDetail.optString("dob");
                                    String categoryy=userDetail.optString("category");
                                    String is_featuredd=userDetail.optString("is_featured");
                                    String fullname=userDetail.optString("fullname");
                                      //selling_cost
                                     int scost=userDetail.optInt("selling_cost");
                                    int pcost=userDetail.optInt("purchese_cost");
                                    //String ratingv=userDetail.getString("avg_rating");
                                    String rating="4";
                                    String more="loadmore";
                                    //item.add(new MyImageModel(String.valueOf(id),String.valueOf(albemid),name,category,String.valueOf(albem_type),image,udate,about_us,String.valueOf(group_id),String.valueOf(is_featured),String.valueOf(status),is_block,String.valueOf(comments),String.valueOf(totallike),String.valueOf(like_unlike),rating,String.valueOf(user_id),more,avatar,fullname));
                                    //item.add(new DataModel(name,image,udate,category,totallike,like_unlike,comments,udate,Float.parseFloat(rating),uid,fullname,avatar,id,IMAGE_TYPE));
                                    //item.add(new MyImageModel(name,image,udate,categoryy,totallike,comments,scost,pcost,Float.parseFloat(ratingv),uid,fullname,avatar,String.valueOf(user_id),more,like_unlike));
                                    item.add(new MyImageModel(String.valueOf(id),String.valueOf(albemid),name,category,String.valueOf(albem_type),image,udate,about_us,String.valueOf(group_id),String.valueOf(is_featured),String.valueOf(status),is_block,String.valueOf(comments),String.valueOf(totallike),String.valueOf(like_unlike),rating,String.valueOf(user_id),more,avatar,fullname,String.valueOf(pcost),String.valueOf(scost)));

                                }
                            }
                             Log.d("allsampledatav",item.toString());
                             dm.setAllItemsInSection(item);
                             Log.d("adm",item.toString());
                             Log.d("dmm",dm.toString());
                            allSampleDatamore.add(dm);
                            Log.d("allsampledatav", allSampleDatamore.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleDatamore.size()+"--"+allSampleDatamore.toString());
                            TreeMap<String,String> item_loadmore=new TreeMap<>();
                            item_loadmore.put("loadmore","loadmore");
                             String type="product";
                           adapterr = new MyImageVideoDataAdapter(getContext(), allSampleDatamore,item_loadmore,type);
                           recycler_view_load_more.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recycler_view_load_more.setAdapter(adapterr);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            pDialog.dismiss();
                            Toast.makeText(getContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
}