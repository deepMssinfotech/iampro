package com.mssinfotech.iampro.co.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.DashboardActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.IntroActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.user.ChangePasswordActivity;
import com.mssinfotech.iampro.co.user.EditProfileActivity;
import com.mssinfotech.iampro.co.user.FriendRequestActivity;
import com.mssinfotech.iampro.co.user.JoinedFriendsActivity;
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
    ImageView menu_btn_dashboard,menu_btn_myprofile,menu_btn_editprofile,menu_btn_changepassword,menu_btn_joinedfriend,menu_btn_friendrequest,menu_btn_myphoto,menu_btn_myvideo,menu_btn_myproduct,menu_btn_myprovide,menu_btn_demand,menu_btn_mymessage,menu_btn_mycart,menu_btn_myselling,menu_btn_mypurchase,menu_btn_share,menu_btn_mywhishlist,menu_btn_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.include_user_menu, parent, false);
        return view;
    }
    private View.OnClickListener dashboardOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_dashboard"))return;
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener myprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_profile"))return;
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener editprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_edit_profile"))return;
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener changepasswordOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_change_password"))return;
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener joinedfriendOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_joined_friends"))return;
            Intent intent = new Intent(getContext(), JoinedFriendsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener friendrequestOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_friend_request"))return;
            Intent intent = new Intent(getContext(), FriendRequestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener myphotoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_image"))return;
            Intent intent = new Intent(getContext(), MyImageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener myvideoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_video"))return;
            Intent intent = new Intent(getContext(), MyVideoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener myproductOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_product"))return;
            Intent intent = new Intent(getContext(), MyProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener myprovideOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_provide"))return;
            Intent intent = new Intent(getContext(), MyProvideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener demandOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_demand"))return;
            Intent intent = new Intent(getContext(), MyDemandActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener mymessageOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_message"))return;
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener mycartOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_cart"))return;
            Intent intent = new Intent(getContext(), CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener mysellingOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_selling"))return;
            Intent intent = new Intent(getContext(), MySellingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener mypurchaseOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_purchase"))return;
            Intent intent = new Intent(getContext(), MyPurchsaeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
           //todo show share option
        }
    };
    private View.OnClickListener mywhishlistOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_my_whishlist"))return;
            Intent intent = new Intent(getContext(), MyWhishlistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //todo logout cose
            PrefManager.logout(getContext());
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);

        }
    };
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        Config.count_whishlist = view.findViewById(R.id.count_whishlist);
        Config.count_friend_request = view.findViewById(R.id.count_friend_request);
        Config.count_cart = view.findViewById(R.id.count_cart);
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

        String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(getContext(),"img_url");
        String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(getContext(),"banner_image");
        TextView username = view.findViewById(R.id.username);
        ImageView userbackgroud = view.findViewById(R.id.userbackgroud);
        CircleImageView userimage = view.findViewById(R.id.userimage);
        username.setText(PrefManager.getLoginDetail(getContext(),"fname") +" "+PrefManager.getLoginDetail(getContext(),"lname"));
        Glide.with(this).load(background).into(userbackgroud);
        Glide.with(this).load(avatar).into(userimage);
    }
}