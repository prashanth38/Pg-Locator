package com.guru.pglocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guru.pglocator.utils.ToastHelper;

public class AdminLogin extends AppCompatActivity {

    EditText etusername,etpassword;
    Button btsubmit,btuser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        etusername=findViewById(R.id.etusername);
        etpassword=findViewById(R.id.etpassword);
        findViewById(R.id.btsubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=etusername.getText().toString();
                String password=etpassword.getText().toString();
                if(username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")){
                    startActivity(new Intent(getApplicationContext(),AdminSuccess.class));
                    finish();

                }else{
                    ToastHelper.toastMsg(getApplicationContext(),"username/password is wrong");
                }
            }
        });

        findViewById(R.id.btuser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }
}
