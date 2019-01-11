package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ForgetActivity extends AppCompatActivity   implements View.OnClickListener{

    TextView btnLogin,btnforgetProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnforgetProcess = findViewById(R.id.btnforgetProcess);
        btnforgetProcess.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btnLogin:
                Intent i_login = new Intent(ForgetActivity.this, LoginActivity.class);
                ForgetActivity.this.startActivity(i_login);
                break;
            case R.id.btnforgetProcess:
                // get click action
                break;
            default:
                break;
        }
    }
}
