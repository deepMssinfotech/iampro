package com.mssinfotech.iampro.co.user;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
public class ChangePasswordActivity extends AppCompatActivity {
    EditText et_currpass,et_newpass,et_confpass;
   Button btn_cpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_change_password));
        btn_cpassword=findViewById(R.id.btn_cpassword);
        et_currpass=findViewById(R.id.et_confpass);
        et_newpass=findViewById(R.id.et_newpass);
        et_confpass=findViewById(R.id.et_confpass);
        btn_cpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }
    public void  changePassword(){
     String currpass=et_currpass.getText().toString();
     String newpass=et_newpass.getText().toString();
      String confpass=et_confpass.getText().toString();
      String url="";
    }
}
