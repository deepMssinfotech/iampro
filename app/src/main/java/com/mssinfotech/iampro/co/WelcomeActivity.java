package com.mssinfotech.iampro.co;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.services.ScheduledService;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout nonloginlayout;
    RelativeLayout loginlayout,loginimage,signupimage;
    SharedPreferences prefrence;
    ImageView btnsignin,btnsignup,btnhome,imguser;
    TextView username;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String fcmMessage,fcmUrl,androidregid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnsignin = findViewById(R.id.imglogin);
        btnsignup = findViewById(R.id.imgsignup);
        btnhome = findViewById(R.id.imghome);
        username = findViewById(R.id.username);
        imguser = findViewById(R.id.imguser);
        Intent intentNotify = getIntent();
        String RedirectURL = intentNotify.getStringExtra("url");

        btnsignin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        btnhome.setOnClickListener(this);
        if(PrefManager.isLogin(this)){
            PrefManager.getCountFromServer(getApplicationContext());
            loginlayout = findViewById(R.id.loginlayout);
            loginlayout.setVisibility(View.VISIBLE);
            //nonloginlayout = findViewById(R.id.nonloginlayout);
            //nonloginlayout.setVisibility(View.GONE);
            loginimage = findViewById(R.id.loginimage);
            signupimage = findViewById(R.id.signupimage);
            loginimage.setVisibility(View.GONE);
            signupimage.setVisibility(View.GONE);
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            username.setText(PrefManager.getLoginDetail(this,"fname"));
            Glide.with(this).load(avatar).into(imguser);
            Log.d(Config.TAG,avatar);
            imguser.setOnClickListener(this);
            if(RedirectURL!=null){
                Fragment fragment = new NotificationActivity();
                function.loadFragment(this,fragment,null);
                /*
                Class<?> myClass = null;
                try {
                    myClass = Class.forName(url);
                    //Activity obj = (Activity) myClass.newInstance();
                    Intent myIntent = new Intent(LoadingActivity.this, myClass); //Maybe here also obj must be needed
                    startActivity(myIntent);
                    finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                */
            }
        }
        //Intent i= new Intent(this, ScheduledService.class);
        //this.startService(i);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    //String click_action = intent.getStringExtra("click_action");
                    //Toast.makeText(getApplicationContext(), click_action, Toast.LENGTH_LONG).show();
                    // Log.e(TAG, "Firebase click_action: " + click_action);
                    //mWebView.loadUrl(click_action);
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("getInstanceId ", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // Log and toast
                        //String msg = getString("msg_token_fmt, token);
                        Log.d("TAG", token);
                        //Toast.makeText(WelcomeActivity.this, token, Toast.LENGTH_SHORT).show();
                        sendRegistrationToServer(token);
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        displayFirebaseRegId();
    }
    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        if(PrefManager.isLogin(this)) {

            String url = Config.API_URL + "ajax.php?type=savefcmregistration&uid="+PrefManager.getLoginDetail(this,"id")+"&token=" + token;
            Log.e("mss get firebase id", "url "+url);
            function.executeUrl(this, "get", url, null);
        }
    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.d("mss unique id", "Firebase reg id: " + regId);
        androidregid = regId;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) { /*to get clicked view id**/
            case R.id.imglogin:
                LoginActivity fragment = new LoginActivity();
                //function.loadFragment(WelcomeActivity.this,fragment,null);

                AppCompatActivity activity =WelcomeActivity.this;
                 fm = activity.getSupportFragmentManager(); //getFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                /*if(args != null){
                    fragment.setArguments(args);
                }*/
               fragmentTransaction = fm.beginTransaction();
                // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(android.R.id.content,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit(); // save the changes
                break;
            case R.id.imgsignup:
                SignupActivity fragmentz = new SignupActivity();
                //function.loadFragment(WelcomeActivity.this,fragmentz,null);

                AppCompatActivity activitys =WelcomeActivity.this;
                 fm = activitys.getSupportFragmentManager(); //getFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                /*if(args != null){
                    fragment.setArguments(args);
                }*/
                 fragmentTransaction = fm.beginTransaction();
                // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(android.R.id.content,fragmentz);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit(); // save the changes

                break;
            case R.id.imghome:
                Intent i_home = new Intent(WelcomeActivity.this,HomeActivity.class);
                WelcomeActivity.this.startActivity(i_home);
                //WelcomeActivity.this.finish();
                break;
            case R.id.imguser:
                ProfileActivity fragments = new ProfileActivity();
                function.loadFragment(WelcomeActivity.this,fragments, null);
                break;
            default:
                break;
        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                String[] separated = message.split("OTP is ");
                //TextView tv = (TextView) findViewById(R.id.txtview);
                //etotp.setText(separated[1]);
                //Log.e("otp is",message+" otp is "+separated[1]);
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    @Override
    public void onBackPressed() {

       /* if (Config.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //this.finish();
           // return;
        }
        Config.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Config.doubleBackToExitPressedOnce = false;
            }
        }, 2000); */
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (Config.doubleBackToExitPressedOnce) {
                super.onBackPressed();
                this.finish();
                return;
            }
            Config.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Config.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
