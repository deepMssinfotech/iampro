package com.mssinfotech.iampro.co.tab;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;

import com.mssinfotech.iampro.co.adapter.UserItemTouchHelper;
import com.mssinfotech.iampro.co.common.SlidingImage_Adapter;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.ImageModel;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class UserFragment extends Fragment implements UserDataAdapter.ItemListener {
    //,UserItemTouchHelper.UserItemTouchHelperListener
    //ArrayList<SectionDataModel> allSampleData=new ArrayList<>();
    ArrayList<UserModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view,recycler_view_load_more;
    LinearLayout ll;
     UserDataAdapter adapter;
      Button btn_load_more;
     int uid;
      public static int start_limit=0;
        public static int end_limit=20;

    ArrayList<MyImageModel> item = new ArrayList<>();
    //MyProductAdapter adapterr;
    MyImageVideoDataAdapter adapterr;
    TreeMap<String,String> item_name=new TreeMap<>();
    ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();

     ImageView luser_iv;

    private View view;
    private boolean add = false;
    private Paint p = new Paint();
     ImageView no_rodr;

    View views;
    //sliderr
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<com.mssinfotech.iampro.co.data.ImageModel> imageModelArrayList;

    public UserFragment() {
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
        View view=inflater.inflate(R.layout.fragment_user, container, false);
         views=view;
        //oolbar =view.findViewById(R.id.toolbar);
        //view.findViewById(R.id.title_tv).setTag("User");

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
        getUser(15);
        my_recycler_view =view.findViewById(R.id.my_recycler_view);
        recycler_view_load_more=view.findViewById(R.id.recycler_view_load_more);
         imageModelArrayList=new ArrayList<>();
        ll=view.findViewById(R.id.ll);
        luser_iv=view.findViewById(R.id.luser_iv);
        no_rodr =view.findViewById(R.id.no_record_found);
        luser_iv.setVisibility(View.VISIBLE);
        luser_iv.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.user));
          btn_load_more=view.findViewById(R.id.btn_load_more);

        luser_iv=view.findViewById(R.id.luser_iv);
               //luser_iv.setBackground(getContext().getResources().getDrawable(R.drawable.user));
        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProductmore();
                //recycler_view_load_more.setVisibility(View.VISIBLE);
                //getAllAlbum();

                //getUser(int limitss);
                start_limit=start_limit+end_limit;
                 getUserMores(start_limit,end_limit);

            }
        });
        getTopSlider();
        //initSwipe();
    }
    private void init() {

        mPager = views.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getContext(),imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)views.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(2 * density);

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
    public void getUser(int limitss){
        final String url = Config.API_URL+ "app_service.php?type=getSelectedUser&limit="+limitss+"&uid="+uid+"&my_id="+uid;

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef", response.toString());
                        if (response.length() > 0) {
                             no_rodr.setVisibility(View.GONE);
                            SectionDataModel dm = new SectionDataModel();
                            dm.setHeaderTitle("User");
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

                                    String name = student.optString("fname");
                                    uid = student.getInt("id");
                                    String identity_type = student.getString("identity_type");
                                    String category = student.getString("category");
                                    String imagev = student.getString("avatar");
                                    String image = Config.AVATAR_URL + "200/200/" + imagev;
                                    String udate = student.getString("udate");
                                    Log.d("pdata", "" + name + "" + category + "" + image + "" + udate);

                                    String total_images = student.optString("total_image");
                                    String total_videos = student.optString("total_video");
                                    String total_users = student.optString("total_friends");
                                    String total_products = student.optString("total_product");
                                    String total_provides = student.optString("total_provide");
                                    String total_demands = student.optString("total_demend");

                                    //is_friend,friend_status,tid,is_block,user_url
                                    String is_friend = student.optString("is_friend");
                                    String friend_status = student.optString("friend_status");
                                    String tid = student.optString("tid");
                                    int is_block = student.optInt("is_block");
                                    String user_url = student.optString("user_url");

                                    //allSampleData.add(new UserModel(uid,name,image,udate,category));
                                    //String total_image,String total_video,String total_friend     //is_friend,friend_status,tid,is_block,user_url
                                    allSampleData.add(new UserModel(uid, name, image, udate, category, total_images, total_videos, total_users, total_products, total_provides, total_demands, is_friend, friend_status, tid, is_block, user_url));

                                }
                                Log.d("bdm", singleItem.toString());
                                dm.setAllItemsInSection(singleItem);
                                Log.d("adm", singleItem.toString());
                                Log.d("dmm", dm.toString());
                                //allSampleData.add(dm);
                                Log.d("allsampledatav", allSampleData.toString());
                                // my_recycler_view.setHasFixedSize(true);
                                Log.d("allSampleDatas", "" + allSampleData.size() + "--" + allSampleData.toString());


                                adapter = new UserDataAdapter(getContext(), allSampleData, UserFragment.this);
                                my_recycler_view.setAdapter(adapter);

                                LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                my_recycler_view.setLayoutManager(manager);

                                //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
                                //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_load_more);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("catch_f", "" + e.getMessage());
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
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                        no_rodr.setVisibility(View.VISIBLE);
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProduct();
    }
    @Override
    public void onItemClick(UserModel item) {
        ProfileActivity fragment = new ProfileActivity();
        Bundle args = new Bundle();
        args.putString("uid", String.valueOf(uid));
        function.loadFragment(getContext(),fragment,args);

    }

    public void getUserMores(int start_limit,int end_limit){
        //final String url = Config.API_URL+ "app_service.php?type=getSelectedUser&limit="+limitss+"&uid="+uid+"&my_id="+uid;
         final String url=Config.API_URL+ "app_service.php?type=getAllUser&name=&uid="+uid+"&my_id="+uid+"&start_limit="+start_limit+"&end_limit="+end_limit;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("User");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                       /* if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        } */
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("fname");
                                uid=student.getInt("id");
                                String identity_type=student.getString("identity_type");
                                String category=student.getString("category");
                                String imagev=student.getString("avatar");
                                String image= Config.AVATAR_URL+"200/200/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);

                                String total_images=student.optString("total_image");
                                String total_videos=student.optString("total_video");
                                String total_users=student.optString("total_friends");
                                String total_products=student.optString("total_product");
                                String total_provides=student.optString("total_provide");
                                String total_demands=student.optString("total_demend");

                                //allSampleData.add(new UserModel(uid,name,image,udate,category));
                                //String total_image,String total_video,String total_friend
                                allSampleData.add(new UserModel(uid,name,image,udate,category,total_images,total_videos,total_users,total_products,total_provides,total_demands));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            // my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());


                            adapter = new UserDataAdapter(getContext(), allSampleData,UserFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);
                             adapter.notifyDataSetChanged();

                            my_recycler_view.setItemAnimator(new DefaultItemAnimator());
                             my_recycler_view.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

                     // ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
                      //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_load_more);

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
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

      /* @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Toast.makeText(getContext(),"swiped",Toast.LENGTH_LONG).show();
        if (viewHolder instanceof UserDataAdapter.ViewHolder) {
        // get the removed item name to display it in snack bar
        String name =allSampleData.get(viewHolder.getAdapterPosition()).getName();

        // backup of removed item for undo purpose
       final UserModel deletedItem =allSampleData.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        // remove the item from recycler view
        adapter.removeItem(viewHolder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(ll, name + " removed from list!", Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", new View.OnClickListener() {
   @Override
   public void onClick(View view) {

        // undo is selected, restore the deleted item
        adapter.restoreItem(deletedItem, deletedIndex);
        }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        }
        } */

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                   // adapter.removeItem(position);
                    Toast.makeText(getContext(),"left",Toast.LENGTH_LONG).show();
                } else {
                   /* removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Country");
                    et_country.setText(countries.get(position));
                    alertDialog.show(); */

                    Toast.makeText(getContext(),"left",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        //c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(my_recycler_view);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

        }