package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener{

    TextView btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnLogin = findViewById(R.id.btnforgetPassword);
        btnLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btnLogin:
                Intent i_login = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(i_login);
                break;
            default:
                break;
        }
    }
}
