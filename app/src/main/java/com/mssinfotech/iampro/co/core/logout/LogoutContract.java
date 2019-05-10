package com.mssinfotech.iampro.co.core.logout;

/**
 * Created by dell on 16/08/2017.
 */
/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface LogoutContract {
    interface View {
        void onLogoutSuccess(String message);

        void onLogoutFailure(String message);
    }

    interface Presenter {
        void logout();
    }

    interface Interactor {
        void performFirebaseLogout();
    }

    interface OnLogoutListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}

