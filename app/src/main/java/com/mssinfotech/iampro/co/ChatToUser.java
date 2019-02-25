package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.ImageAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserChatAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.model.ChatList;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.models.Chat;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatToUser extends AppCompatActivity implements UserChatAdapter.ItemListener{
  String id;
  ArrayList<String> item_name=new ArrayList<>();
   RecyclerView recycler_view_chat;
   EditText et_chat;
    ImageView btnSend;
    ArrayList<ChatList> allSampleDatamore=new ArrayList<>();
    UserChatAdapter adapter;
    String myid;

    String mtype="mine";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_to_user);
        recycler_view_chat=findViewById(R.id.recycler_view_chat);
        et_chat=findViewById(R.id.et_chat);
        btnSend=findViewById(R.id.btnSend);
       if (!allSampleDatamore.isEmpty()){
             allSampleDatamore.clear();
        }
        id=getIntent().getExtras().getString("id");
        if (PrefManager.isLogin(ChatToUser.this)){
            myid=PrefManager.getLoginDetail(ChatToUser.this,"id");
        }
        getUserDetail(id);
        getChatList(id);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                //allSampleDatamore.add(new ChatList(from_id,to_id,avatar,messageType,msg,udate));
            }
        });
         new CheckNewChat().start();
    }
      public void getUserDetail(String id){
          String url="https://www.iampro.co/api/app_service.php?type=get_user_detail&id="+id;
          RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
          JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,null,
                  new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          System.out.println(response);
                          try {
                              String id = response.getString("id");
                              String  role = response.getString("role");
                              String username = response.getString("username");
                              String avatar= response.getString("avatar");
                              String fullname = response.getString("fullname");

                               getSupportActionBar().setIcon(R.drawable.iampro);

                               getSupportActionBar().setTitle("Chat with "+fullname);
                          }
                           catch(Exception e) {
                           Toast.makeText(getApplicationContext(),"ud"+e.getMessage(),Toast.LENGTH_LONG).show();
                           }
                      }
                  },
                  new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {

                      }
                  });
          requestQueue.add(jsObjRequest);
    }

    public void getChatList(String id){
          String myid= PrefManager.getLoginDetail(this,"id");
        String url="https://www.iampro.co/api/chat.php?type=fetchChat&lang=en&uid="+id+"&myid="+myid;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray jsonArray=response.getJSONArray("list");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject student1 =jsonArray.getJSONObject(i);
                                String from_id= student1.optString("from_id");
                                String to_id = student1.optString("to_id");
                                 String avatar = student1.optString("avatar");
                                 String messageType = student1.optString("messageType");
                                   mtype=messageType;
                                 String msg = student1.optString("msg");
                                 String udate = student1.optString("udate");
                                allSampleDatamore.add(new ChatList(from_id,to_id,avatar,messageType,msg,udate));
                            }
                            adapter = new UserChatAdapter(getApplicationContext(),allSampleDatamore,ChatToUser.this,mtype);
                            recycler_view_chat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            recycler_view_chat.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recycler_view_chat.scrollToPosition(allSampleDatamore.size() - 1);
                        }
                        catch(Exception e) {
                            Toast.makeText(getApplicationContext(),"getc"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"getc"+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onItemClick(ChatList item) {

    }
    public void sendMessage(){
        final String msg=et_chat.getText().toString();
        String url="https://www.iampro.co/api/chat.php?type=saveChat&uid="+id+"&msg="+msg+"&myid="+myid+"&abc_chat=0";
         RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                allSampleDatamore.add(new ChatList(myid,id,"avatar","sent",msg,new Date().toString()));
                adapter = new UserChatAdapter(getApplicationContext(),allSampleDatamore,ChatToUser.this,"sent");
                recycler_view_chat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycler_view_chat.setAdapter(adapter);
                recycler_view_chat.scrollToPosition(allSampleDatamore.size() - 1);
                //adapter.notifyDataSetChanged();
                getChatList(id);
                et_chat.setText("");
                 Log.d("allSampleDatamore",""+ allSampleDatamore+"\t"+response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("type","saveChat");
                MyData.put("uid",id);
                MyData.put("msg",msg);
                MyData.put("myid",myid);
                MyData.put("abc_chat","0");
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }
    public void checkNewChat(){
        String url="https://www.iampro.co/api/chat.php?type=checkNewChat&lang=en&uid="+id+"&myid="+myid;
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               if(response!=null || response!="" || !response.equalsIgnoreCase("") || !response.isEmpty()){
                   try {
                       JSONObject jsonObject = new JSONObject(response);

                       //String mycount=jsonObject.optString("mycount");
                        String status=jsonObject.optString("status");
                         if(!status.equalsIgnoreCase("error")) {
                             if(!allSampleDatamore.isEmpty())
                                   allSampleDatamore.clear();
                             JSONArray jsonArray = jsonObject.getJSONArray("list");
                             for (int i = 0; i < jsonArray.length(); i++) {
                                 JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                 String from_id = jsonObject1.getString("from_id");
                                 String to_id = jsonObject1.optString("to_id");
                                 String avatar = jsonObject1.getString("avatar");
                                 String messageType = jsonObject1.getString("messageType");
                                 mtype = messageType;
                                 String msg = jsonObject1.getString("msg");
                                 String udate = jsonObject1.getString("udate");
                                 allSampleDatamore.add(new ChatList(from_id, to_id, avatar, messageType, msg, udate));
                             }

                             adapter = new UserChatAdapter(getApplicationContext(), allSampleDatamore, ChatToUser.this, mtype);
                             recycler_view_chat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                             recycler_view_chat.setAdapter(adapter);
                             adapter.notifyDataSetChanged();
                         }
                   }
                   catch(JSONException e){
                       Log.d("n_chat",""+e.getMessage());
                       Toast.makeText(getApplicationContext(),"nc"+e.getMessage(),Toast.LENGTH_LONG).show();
                   }
               }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"nc"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("type","checkNewChat");
                MyData.put("lang","en");
                MyData.put("uid",id);
                MyData.put("myid",myid);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }
  class CheckNewChat extends Thread{
      @Override
      public void run() {
          checkNewChat();
          try {
              Thread.sleep(5000);
          }
          catch (InterruptedException e){
              e.printStackTrace();
          }
      }
  }
}
