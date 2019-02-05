package com.mssinfotech.iampro.co.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.DashboardActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.IntroActivity;
import com.mssinfotech.iampro.co.LoginActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.SignupActivity;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.user.ChangePasswordActivity;
import com.mssinfotech.iampro.co.user.EditProfileActivity;
import com.mssinfotech.iampro.co.user.FriendRequestActivity;
import com.mssinfotech.iampro.co.user.JoinFriendActivity;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyPurchsaeActivity;
import com.mssinfotech.iampro.co.user.MySellingActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.MyWhishlistActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMenuActivity  extends Fragment {

    GridView mGridView;;
    ImageView menu_btn_dashboard_nonuser,menu_btn_login,menu_btn_register,menu_btn_search,menu_btn_shareit,menu_btn_review,menu_btn_wallet,menu_btn_browser;
    ImageView menu_btn_dashboard,menu_btn_myprofile,menu_btn_editprofile,menu_btn_changepassword,menu_btn_joinedfriend,menu_btn_friendrequest,menu_btn_myphoto,menu_btn_myvideo,menu_btn_myproduct,menu_btn_myprovide,menu_btn_demand,menu_btn_mymessage,menu_btn_mycart,menu_btn_myselling,menu_btn_mypurchase,menu_btn_share,menu_btn_mywhishlist,menu_btn_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.include_user_menu, parent, false);
        return view;
    }

    public void removeFragment(){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        UserMenuActivity userMenufragment = new UserMenuActivity();
        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(userMenufragment);
        fragmentTransaction.commit();
        function.finishFunction(getContext());
    }
    private View.OnClickListener dashboardOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_dashboard"))return;
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_profile"))return;
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener editprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_edit_profile"))return;
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener changepasswordOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_change_password"))return;
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener joinedfriendOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if(function.isSamePage("activity_joined_friends"))return;
            Intent intent = new Intent(getContext(), JoinFriendActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener friendrequestOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_friend_request"))return;
            Intent intent = new Intent(getContext(), FriendRequestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myphotoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_image"))return;
            Intent intent = new Intent(getContext(), MyImageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myvideoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_video"))return;
            Intent intent = new Intent(getContext(), MyVideoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myproductOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_product"))return;
            Intent intent = new Intent(getContext(), MyProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myprovideOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_provide"))return;
            Intent intent = new Intent(getContext(), MyProvideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener demandOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_demand"))return;
            Intent intent = new Intent(getContext(), MyDemandActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener mymessageOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_message"))return;
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener mycartOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_cart"))return;
            Intent intent = new Intent(getContext(), CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener mysellingOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_selling"))return;
            Intent intent = new Intent(getContext(), MySellingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener mypurchaseOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_purchase"))return;
            Intent intent = new Intent(getContext(), MyPurchsaeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String shareBody = "Share to you friend and family to join "+Config.URL_ROOT;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Iampro App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            removeFragment();
        }
    };
    private View.OnClickListener mywhishlistOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_whishlist"))return;
            Intent intent = new Intent(getContext(), MyWhishlistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            PrefManager.logout(getContext());
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_login"))return;
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_register"))return;
            Intent intent = new Intent(getContext(), SignupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_whishlist"))return;
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener reviewOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String appPackageName = getActivity().getPackageName(); // package name of the app
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    };
    private View.OnClickListener walletOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            function.OpenWallet(getContext());
        }
    };
    private View.OnClickListener browserOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            function.OpenBrowser(getContext(),Config.URL_ROOT);
        }
    };
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        LinearLayout nonuser_menu = view.findViewById(R.id.nonuser_menu);
        LinearLayout user_menu = view.findViewById(R.id.user_menu);
        Config.count_whishlist = view.findViewById(R.id.count_whishlist);
        Config.count_friend_request = view.findViewById(R.id.count_friend_request);
        Config.count_cart = view.findViewById(R.id.count_cart);
        TextView username = view.findViewById(R.id.username);
        if(PrefManager.isLogin(getContext())) {
            menu_btn_dashboard = view.findViewById(R.id.menu_btn_dashboard);
            menu_btn_myprofile = view.findViewById(R.id.menu_btn_myprofile);
            menu_btn_editprofile = view.findViewById(R.id.menu_btn_editprofile);
            menu_btn_changepassword = view.findViewById(R.id.menu_btn_changepassword);
            menu_btn_joinedfriend = view.findViewById(R.id.menu_btn_joinedfriend);
            menu_btn_friendrequest = view.findViewById(R.id.menu_btn_friendrequest);
            menu_btn_myphoto = view.findViewById(R.id.menu_btn_myphoto);
            menu_btn_myvideo = view.findViewById(R.id.menu_btn_myvideo);
            menu_btn_myproduct = view.findViewById(R.id.menu_btn_myproduct);
            menu_btn_myprovide = view.findViewById(R.id.menu_btn_myprovide);
            menu_btn_demand = view.findViewById(R.id.menu_btn_demand);
            menu_btn_mymessage = view.findViewById(R.id.menu_btn_mymessage);
            menu_btn_mycart = view.findViewById(R.id.menu_btn_mycart);
            menu_btn_myselling = view.findViewById(R.id.menu_btn_myselling);
            menu_btn_mypurchase = view.findViewById(R.id.menu_btn_mypurchase);
            menu_btn_share = view.findViewById(R.id.menu_btn_share);
            menu_btn_mywhishlist = view.findViewById(R.id.menu_btn_mywhishlist);
            menu_btn_logout = view.findViewById(R.id.menu_btn_logout);
            menu_btn_dashboard.setOnClickListener(dashboardOnClickListener);
            menu_btn_myprofile.setOnClickListener(myprofileOnClickListener);
            menu_btn_editprofile.setOnClickListener(editprofileOnClickListener);
            menu_btn_changepassword.setOnClickListener(changepasswordOnClickListener);
            menu_btn_joinedfriend.setOnClickListener(joinedfriendOnClickListener);
            menu_btn_friendrequest.setOnClickListener(friendrequestOnClickListener);
            menu_btn_myphoto.setOnClickListener(myphotoOnClickListener);
            menu_btn_myvideo.setOnClickListener(myvideoOnClickListener);
            menu_btn_myproduct.setOnClickListener(myproductOnClickListener);
            menu_btn_myprovide.setOnClickListener(myprovideOnClickListener);
            menu_btn_demand.setOnClickListener(demandOnClickListener);
            menu_btn_mymessage.setOnClickListener(mymessageOnClickListener);
            menu_btn_mycart.setOnClickListener(mycartOnClickListener);
            menu_btn_myselling.setOnClickListener(mysellingOnClickListener);
            menu_btn_mypurchase.setOnClickListener(mypurchaseOnClickListener);
            menu_btn_share.setOnClickListener(shareOnClickListener);
            menu_btn_mywhishlist.setOnClickListener(mywhishlistOnClickListener);
            menu_btn_logout.setOnClickListener(logoutOnClickListener);
            String avatar = Config.AVATAR_URL + "250/250/" + PrefManager.getLoginDetail(getContext(), "img_url");
            String background = Config.AVATAR_URL + "h/250/" + PrefManager.getLoginDetail(getContext(), "banner_image");

            ImageView userbackgroud = view.findViewById(R.id.userbackgroud);
            CircleImageView userimage = view.findViewById(R.id.userimage);
            username.setText(PrefManager.getLoginDetail(getContext(), "fname") + " " + PrefManager.getLoginDetail(getContext(), "lname"));
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            nonuser_menu.setVisibility(View.GONE);
            user_menu.setVisibility(View.VISIBLE);
        }else{
            menu_btn_dashboard_nonuser = view.findViewById(R.id.menu_btn_dashboard_nonuser);
            menu_btn_login = view.findViewById(R.id.menu_btn_login);
            menu_btn_register = view.findViewById(R.id.menu_btn_register);
            menu_btn_search = view.findViewById(R.id.menu_btn_search);
            menu_btn_shareit = view.findViewById(R.id.menu_btn_shareit);
            menu_btn_review = view.findViewById(R.id.menu_btn_review);
            menu_btn_wallet = view.findViewById(R.id.menu_btn_wallet);
            menu_btn_browser = view.findViewById(R.id.menu_btn_browser);
            menu_btn_dashboard_nonuser.setOnClickListener(dashboardOnClickListener);
            menu_btn_login.setOnClickListener(loginOnClickListener);
            menu_btn_register.setOnClickListener(registerOnClickListener);
            menu_btn_search.setOnClickListener(searchOnClickListener);
            menu_btn_shareit.setOnClickListener(shareOnClickListener);
            menu_btn_review.setOnClickListener(reviewOnClickListener);
            menu_btn_wallet.setOnClickListener(walletOnClickListener);
            menu_btn_browser.setOnClickListener(browserOnClickListener);
            username.setText("Welcome To Iampro");
            nonuser_menu.setVisibility(View.VISIBLE);
            user_menu.setVisibility(View.GONE);
        }
    }

}