package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    TextView btnforgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnforgetPassword = findViewById(R.id.btnforgetPassword);
        btnforgetPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btnforgetPassword:
                Intent i_login = new Intent(LoginActivity.this, ForgetActivity.class);
                LoginActivity.this.startActivity(i_login);
                break;
            default:
                break;
        }
    }
}
