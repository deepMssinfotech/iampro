package com.mssinfotech.iampro.co.core.registration;

/**
 * Created by dell on 16/08/2017.
 */
import android.app.Activity;
import com.google.firebase.auth.FirebaseUser;
/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:36 AM
 * Project: FirebaseChat
 */

public interface RegisterContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void register(Activity activity, String email, String password);
    }

    interface Interactor {
        void performFirebaseRegistration(Activity activity, String email, String password);
    }

    interface OnRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);

        void onFailure(String message);
    }
}
