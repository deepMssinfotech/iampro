package com.mssinfotech.iampro.co.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mssinfotech.iampro.co.adapter.MyImageAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.swipecontroller.SwipeController;
import com.mssinfotech.iampro.co.swipecontroller.SwipeControllerActions;
import com.mssinfotech.iampro.co.tab.UserFragment;
import com.mssinfotech.iampro.co.user.JoinFriendActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.JoinFriendAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.JoinFriendItem;
import com.mssinfotech.iampro.co.helper.JoinFriendItemTouchHelper;
import com.mssinfotech.iampro.co.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinFriendActivity extends Fragment implements UserDataAdapter.ItemListener {
    //JoinFriendItemTouchHelper.RecyclerItemTouchHelperListener,
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private RecyclerView recyclerView;
    private List<JoinFriendItem> JoinFriendItemList;
    private JoinFriendAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private static String NOTIFY_URL = "";
    private String URL_FEED = "",uid = "";
    Context context;
    Intent intent;
     LinearLayout ll_header;
    View view;
    SwipeController swipeController = null;
     ArrayList<UserModel> allSampleData=new ArrayList<>();
         UserDataAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_joinfriend, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        String id;
        context = getContext();
        uid= PrefManager.getLoginDetail(getContext(),"id");
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_joinfriend));
        username = view.findViewById(R.id.username);
        userimage = view.findViewById(R.id.userimage);
        ll_header=view.findViewById(R.id.ll_header);
        userbackgroud = view.findViewById(R.id.userbackgroud);
        try {
            id = intent.getStringExtra("uid");
        }
        catch (Exception e){
            id=uid;
        }
        //getUser(15);
        uid= PrefManager.getLoginDetail(getContext(),"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(getContext(),"fname");
            String lname=PrefManager.getLoginDetail(getContext(),"lname");
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(getContext(),"img_url");
            String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(getContext(),"banner_image");
            username.setText("My Friends");
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            userbackgroud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"banner_image"), null);
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"img_url"), null);
                }
            });
            PrefManager.updateUserData(getContext(),null);
        }else{
            uid= id;
            gteUsrDetail(id);
        }

        IncludeShortMenu includeShortMenu = view.findViewById(R.id.includeShortMenu);
        //includeShortMenu.updateCounts(getContext(),uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);

        NOTIFY_URL  = Config.API_URL+"app_service.php?type=view_friend_list&id="+ PrefManager.getLoginDetail(getContext(),"id")+"&my_id="+ PrefManager.getLoginDetail(getContext(),"id")+"&status=2";
        recyclerView = view.findViewById(R.id.recycler_view);
        JoinFriendItemList = new ArrayList<JoinFriendItem>();
        //mAdapter = new JoinFriendAdapter(this, JoinFriendItemList);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
       // ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new JoinFriendItemTouchHelper(0, ItemTouchHelper.LEFT,JoinFriendActivity.this);
        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        // making http call and fetching menu json
        prepareWhishList();
        getJoinedFriend();

        /*swipeController = new SwipeController(JoinFriendActivity.this.getContext(),new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                //mAdapter.players.remove(position);
                //mAdapter.notifyItemRemoved(position);
                //mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                Toast.makeText(getContext(),"Right Clicked"+position,Toast.LENGTH_LONG).show();

            }
            public void onLeftClicked(int position) {
                Toast.makeText(getContext(),"Left Clicked"+position,Toast.LENGTH_LONG).show();
            }
        }); */

       /* ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        }); */

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
                            String fname=result.optString("fname");
                            String lname=result.optString("lname");
                            final String avatarX=result.getString("avatar");
                            final String backgroundX=result.getString("banner_image");
                            username = view.findViewById(R.id.username);
                            userimage = view.findViewById(R.id.userimage);
                            userbackgroud = view.findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Friends");
                            Glide.with(getContext()).load(Config.AVATAR_URL+"h/250/"+backgroundX).apply(Config.options_background).into(userbackgroud);
                            Glide.with(getContext()).load(Config.AVATAR_URL+"250/250/"+avatarX).apply(Config.options_avatar).into(userimage);
                            userbackgroud.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+backgroundX, null);
                                }
                            });
                            userimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+avatarX, null);
                                }
                            });
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    /**
     * method make volley network call and parses json
     */
    private void prepareWhishList() {
        //Log.d(Config.TAG,WHISH_LIST_URL);
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET,
                NOTIFY_URL  , null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    parseJsonFeed(response);
                   // mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "Empty Record!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Config.TAG,"onErrorResponse 96 : "+error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
    private void parseJsonFeed(JSONArray response) {
        try {
            //JSONArray feedArray = response.getJSONArray("data");
            if(response.length()>0) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject feedObj = (JSONObject) response.get(i);
                    JoinFriendItem item = new JoinFriendItem();

                    String image_path = "";
                    JSONObject user_detail = feedObj.getJSONObject("user_detail");
                    String user_image = Config.AVATAR_URL + "80/80/" + user_detail.getString("avatar");
                    item.setId(feedObj.getInt("id"));
                    item.setAvatar(user_image);
                    //item.setUser_id(user_detail.getInt("user_id"));
                    //item.setUser_id(user_detail.getInt("id"));
                    item.setUser_id(Integer.parseInt(user_detail.optString("id")));
                    item.setFriend_id(feedObj.getInt("friend_id"));
                    item.setCategory(user_detail.getString("category"));
                    item.setCity(user_detail.getString("city"));
                    item.setState(user_detail.getString("state"));
                    item.setCountry(user_detail.getString("country"));
                    item.setFullname(user_detail.getString("fullname"));
                    item.setUsername(user_detail.getString("username"));
                    item.setTotal_friend(feedObj.getInt("total_friend"));
                    item.setTotal_img(feedObj.getInt("total_img"));
                    item.setTotal_product(feedObj.getInt("total_product"));
                    item.setTotal_product_provide(feedObj.getInt("total_product_provide"));
                    item.setTotal_product_demand(feedObj.getInt("total_product_demand"));
                    item.setTotal_video(feedObj.getInt("total_video"));

                    JoinFriendItemList.add(item);
                }
            }else{
                ImageView no_rodr = view.findViewById(R.id.no_record_found);
                no_rodr.setVisibility(View.VISIBLE);
            }

            // notify data changes to list adapater
        } catch (JSONException e) {

            Log.d(Config.TAG,"printStackTrace 167" + e.getMessage() + "  Error Message");
            e.printStackTrace();
        }
    }

    /*@Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof JoinFriendAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            Integer id = JoinFriendItemList.get(viewHolder.getAdapterPosition()).getId();
            Integer Userid = JoinFriendItemList.get(viewHolder.getAdapterPosition()).getUser_id();
            Integer Friendid = JoinFriendItemList.get(viewHolder.getAdapterPosition()).getFriend_id();

            // backup of removed item for undo purpose
            final JoinFriendItem deletedItem = JoinFriendItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            //mAdapter.removeItem(viewHolder.getAdapterPosition());
            //String url=Config.API_URL+"app_service.php?type=delete_friend&id="+Friendid.toString()+"&tid="+id.toString();
            //String responc = function.executeUrl(getContext(),"get",url,null);
            //Log.e(Config.TAG,"result : "+responc+"url - "+url);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(ll_header, "Notification removed ", Snackbar.LENGTH_LONG);
            snackbar.setAction("Close", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    //mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    } */

    public void getJoinedFriend(){
        String url="https://www.iampro.co/api/app_service.php?type=view_friend_list&id="+uid+"&status=2&uid="+uid+"&my_id="+uid;
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
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                  JSONObject user_detaiis=student.getJSONObject("user_detail");
                                  //friendstatus
                                JSONObject friendstatus=student.getJSONObject("friendstatus");
                                    String id=user_detaiis.getString("id");
                                 String fname=user_detaiis.getString("fname");
                                 String lname=user_detaiis.getString("lname");
                                String avatar=user_detaiis.getString("avatar");
                                String category=user_detaiis.getString("category");

                                    //JoinFriendItemList.add(new JoinFriendItem(avatar,fname,category));

                                String name = user_detaiis.optString("fname");
                                int uid=user_detaiis.getInt("id");
                                //String identity_type=student.getString("identity_type");
                                String categorys=user_detaiis.getString("category");
                                String imagev=user_detaiis.getString("avatar");
                                String image= Config.AVATAR_URL+"200/200/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);

                                String total_images=student.optString("total_img");
                                String total_videos=student.optString("total_video");
                                String total_users=student.optString("total_friend");
                                String total_products=student.optString("total_product");
                                String total_provides=student.optString("total_product_provide");
                                String total_demands=student.optString("total_product_demend");

                                //is_friend,friend_status,tid,is_block,user_url
                                String is_friend=student.optString("is_friend");
                                String friend_status=friendstatus.optString("status");
                                String tid=student.optString("fid");
                                int is_blocks=student.optInt("is_block");
                                String user_url=user_detaiis.optString("fullname");
                                JoinFriendItemList.add(new JoinFriendItem(uid,name,image,udate,categorys,total_images,total_videos,total_users,total_products,total_provides,total_demands,is_friend,friend_status,tid,is_blocks,user_url));

                            }
                            Log.d("bdm",singleItem.toString());
                            // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("allsampledatav",JoinFriendItemList.toString());
                             mAdapter= new JoinFriendAdapter(getContext(),JoinFriendItemList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            //recyclerView.setItemAnimator(new DefaultItemAnimator());
                            //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setNestedScrollingEnabled(false);
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
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }

    public void getUser(int limitss){
        //final String url = "https://www.iampro.co/api/app_service.php?type=getSelectedUser&limit="+limitss+"&uid="+uid+"&my_id="+uid;
         String url="https://www.iampro.co/api/app_service.php?type=view_friend_list&id="+uid+"&status=2&uid="+uid+"&my_id="+uid;
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
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                 JSONObject user_detail=student.getJSONObject("user_detail");
                                String name=user_detail.optString("fname");
                                int uid=user_detail.getInt("id");
                                //String identity_type=student.getString("identity_type");
                                String category=user_detail.getString("category");
                                String imagev=user_detail.getString("avatar");
                                String image= Config.AVATAR_URL+"200/200/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);

                                String total_images=student.optString("total_image");
                                String total_videos=student.optString("total_video");
                                String total_users=student.optString("total_friends");
                                String total_products=student.optString("total_product");
                                String total_provides=student.optString("total_provide");
                                String total_demands=student.optString("total_demend");

                                String is_friend=student.optString("is_friend");
                                String friend_status=student.optString("friend_status");
                                String tid=student.optString("tid");
                                int is_block=student.optInt("is_block");
                                String user_url=student.optString("user_url");

                                //allSampleData.add(new UserModel(uid,name,image,udate,category));
                                //String total_image,String total_video,String total_friend
                                //allSampleData.add(new UserModel(uid,name,image,udate,category,total_images,total_videos,total_users,total_products,total_provides,total_demands));
                                allSampleData.add(new UserModel(uid,name,image,udate,category,total_images,total_videos,total_users,total_products,total_provides,total_demands,is_friend,friend_status,tid,is_block,user_url));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            // my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());


                            adapter = new UserDataAdapter(getContext(), allSampleData,JoinFriendActivity.this);
                            recyclerView.setAdapter(adapter);

                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);

                            //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
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
        //getProduct();
    }
    @Override
    public void onItemClick(UserModel item) {
    Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();
    }
}
