package com.mssinfotech.iampro.co.tab;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.mssinfotech.iampro.co.adapter.MyVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.adapter.VideoAdapter;
import com.mssinfotech.iampro.co.common.SlidingImage_Adapter;
import com.mssinfotech.iampro.co.data.ImageModel;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
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

public class VideoFragment extends Fragment implements VideoAdapter.ItemListener{
    ArrayList<DataModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view,recycler_view_load_more;
    VideoAdapter adapter;
    public static final String VIDEO_TYPE="video";
    int uid;
    Button btn_load_more;
    HashMap<String,String> item_name=new HashMap<>();
    ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();
    MyVideoDataAdapter adapterr;
     ImageView lvideo_iv;
    ImageView no_rodr;
     View views;
    //sliderr
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<com.mssinfotech.iampro.co.data.ImageModel> imageModelArrayList;

    public VideoFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view=inflater.inflate(R.layout.fragment_video, container, false);
             views=view;
            //oolbar =view.findViewById(R.id.toolbar);
        //view.findViewById(R.id.title_tv).setTag("Video");
            return view;
        }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //createDummyData();
            if(PrefManager.isLogin(getContext()))
             uid= Integer.parseInt(PrefManager.getLoginDetail(getContext(),"id"));
            getVideo();
            my_recycler_view =view.findViewById(R.id.my_recycler_view);
            recycler_view_load_more=view.findViewById(R.id.recycler_view_load_more);
            lvideo_iv=view.findViewById(R.id.lvideo_iv);
            lvideo_iv.setVisibility(View.VISIBLE);
             no_rodr =view.findViewById(R.id.no_record_found);
            lvideo_iv.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.latestvideo));
            btn_load_more=view.findViewById(R.id.btn_load_more);
             imageModelArrayList=new ArrayList<>();
            btn_load_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllAlbum();
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
    public void getVideo(){
        final String url = Config.API_URL+ "app_service.php?type=all_item&name=video&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
        pDialog.setMessage("Loading...!");
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
                            dm.setHeaderTitle("Video ");
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
                                    int id = student.optInt("id");
                                    //int added_by=student.getInt("albumid");
                                    String name = student.optString("name");
                                    String categoryv = student.optString("category");
                                    String imagev = student.optString("v_image");
                                    String image = Config.URL_ROOT + "uploads/v_image/300/250/" + imagev;
                                    String udate = student.optString("udate");
                                    Log.d("pdata", "" + name + "" + categoryv + "" + image + "" + udate);
                                    int totallike = student.optInt("totallike");
                                    int comments = student.optInt("comments");

                                    String daysago = student.optString("ago");

                                    String rating = student.getString("rating");
                                    float ratingv = Float.parseFloat(rating);

                                    JSONObject userDetail = student.getJSONObject("user_detail");
                                    int uid = userDetail.getInt("id");
                                    String fullname = userDetail.getString("fullname");
                                    String avatar = Config.AVATAR_URL + "250/250/" + userDetail.getString("avatar");

                                    //SectionDataModel dm = new SectionDataModel();
                                    //dm.setHeaderTitle("Section " + i);
                                    // Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                    //singleItem.add(new SingleItemModel(name,image,udate));
                                    //allSampleData.add(new DataModel(name,image,udate,categoryv));
                                    int isliked = student.getInt("like_unlike");
                                    allSampleData.add(new DataModel(name, image, udate, categoryv, totallike, isliked, comments, daysago, ratingv, uid, fullname, avatar, id, VIDEO_TYPE));
                                }
                                Log.d("bdm", singleItem.toString());
                                dm.setAllItemsInSection(singleItem);
                                Log.d("adm", singleItem.toString());
                                Log.d("dmm", dm.toString());
                                // allSampleData.add(dm);
                                Log.d("allsampledatav", allSampleData.toString());

                          /*  my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapter); */

                                adapter = new VideoAdapter(getContext(), allSampleData, VideoFragment.this);
                                my_recycler_view.setAdapter(adapter);

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
                        else{
                            no_rodr.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                        pDialog.dismiss();
                        no_rodr.setVisibility(View.VISIBLE);
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getUser();
    }
    @Override
    public void onItemClick(DataModel item) {
        Toast.makeText(getContext(), item.getName()+ " is clicked", Toast.LENGTH_SHORT).show();
    }

    public void getAllAlbum(){
        //String url=Config.API_URL+ "app_service.php?type=getAlbemsListt&search_type=video&uid="+uid;
        String url=Config.API_URL+ "app_service.php?type=get_category&name=VIDEO&uid="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
        pDialog.setMessage("Loading...!");
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
                                getVideoMores(data);
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
    public void getVideo(final String aid){
        String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=video&uid="+uid+"&my_id="+uid+"&album_id="+aid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
        pDialog.setMessage("Loading...!");
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
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(item_name.get(aid));
                        dm.setAlbemId(aid);
                        dm.setMore("loadmore");
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                String name1=student.getString("name");
                                //tv_category.setText(name1.toString());
                                JSONArray jsonArrayPics=student.getJSONArray("pics");
                                String added_byy=student.optString("added_by");
                                Log.d("picssss",jsonArrayPics.toString());
                                //JSONObject picss= jsonArrayPics.getJSONObject(0);
                                //Log.d("picssss11",""+picss.toString());
                                //String idd=picss.getString("albemid");
                                //Log.d("picssss1",""+idd);
                                for (int j=0;j<jsonArrayPics.length();j++){
                                    JSONObject pics=jsonArrayPics.getJSONObject(j);
                                    String id=pics.getString("id");

                                    String albemid=pics.optString("albemid");
                                    String name=pics.optString("name");
                                    String category=pics.optString("category");

                                    String albem_type=pics.optString("albem_type");
                                    String imagev=pics.optString("image");
                                    String imagee = imagev.substring(0, imagev.lastIndexOf("."));
                                    String image=imagee+".jpg";
                                    Log.d("imagebb",""+image);
                                    String udate=pics.optString("udate");
                                    String about_us=pics.optString("about_us");
                                    String group_id=pics.optString("group_id");
                                    String is_featured=pics.optString("is_featured");
                                    String status=pics.optString("status");
                                    String is_block=pics.optString("is_block");
                                    String comments=pics.optString("comments");
                                    String totallike=pics.optString("totallike");
                                    String like_unlike=pics.optString("like_unlike");
                                    String rating=pics.optString("rating");
                                    String more="loadmore";
                                    item.add(new MyImageModel(id,albemid,name,category,albem_type,image,udate,about_us,group_id,is_featured,status,is_block,comments,totallike,like_unlike,rating,added_byy,more));

                                }
                            }
                            Log.d("bdm",singleItem.toString());
                            // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("allsampledatav",item.toString());
                           /* adapter = new MyVideoAdapter(getApplicationContext(),item,MyVideoActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setNestedScrollingEnabled(false); */


                            dm.setAllItemsInSection(item);
                            Log.d("adm",item.toString());
                            Log.d("dmm",dm.toString());
                            allSampleDatamore.add(dm);
                            Log.d("allsampledatav", allSampleDatamore.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleDatamore.size()+"--"+allSampleDatamore.toString());
                            adapterr = new MyVideoDataAdapter(getContext(),allSampleDatamore,item_name);
                            recycler_view_load_more.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            recycler_view_load_more.setAdapter(adapterr);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                            pDialog.dismiss();
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

    public void getVideoMores(final String cname){
        final ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
        pDialog.setMessage("Loading...!");
        pDialog.show();
        String url=null;
        try {
            String query = URLEncoder.encode(cname, "utf-8");
            //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
            // String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
          url = Config.API_URL+ "app_service.php?type=search_all_items&search_type=VIDEO&category=" + query+ "&search_data=&uid=" + uid + "&my_id=" + uid;
            // Initialize a new RequestQueue instance
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url = Config.API_URL+ "app_service.php?type=search_all_items&search_type=VIDEO&category=" +cname+ "&search_data=&uid=" + uid + "&my_id=" + uid;

        }
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
                        ArrayList<MyImageModel> item = new ArrayList<>();
                         //MyImageModel imageModel=new MyImageModel();
                        //ArrayList<DataModel> item = new ArrayList<>();
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
                                JSONArray jsonArrayPics=student.getJSONArray("img_detail");
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
                                    String rating=pics.optString("rating");
                                    String is_block=pics.optString("is_block");
                                    String v_image=pics.optString("v_image");
                                     Log.d("VV_image",""+v_image);

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
                                    String more="loadmore";
                                    item.add(new MyImageModel(String.valueOf(id),String.valueOf(albemid),name,category,String.valueOf(albem_type),image,udate,about_us,String.valueOf(group_id),String.valueOf(is_featured),String.valueOf(status),is_block,String.valueOf(comments),String.valueOf(totallike),String.valueOf(like_unlike),rating,String.valueOf(user_id),more,avatar,fullname,v_image,"video",1));
                                    //item.add(new DataModel(name,image,udate,category,totallike,like_unlike,comments,udate,Float.parseFloat(rating),uid,fullname,avatar,id,IMAGE_TYPE));
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
                            HashMap<String,String> item_loadmore=new HashMap<>();
                            item_loadmore.put("loadmore","loadmore");
                            //adapterr = new MyImageVideoDataAdapter(getContext(), allSampleDatamore,item_loadmore);
                            //recycler_view_load_more.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //recycler_view_load_more.setAdapter(adapterr);
                            adapterr = new MyVideoDataAdapter(getContext(),allSampleDatamore,item_loadmore);
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