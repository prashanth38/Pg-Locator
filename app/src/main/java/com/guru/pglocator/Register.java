package com.guru.pglocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.PgOwner;
import com.guru.pglocator.utils.ToastHelper;

public class Register  extends AppCompatActivity {

    Spinner spinner;
    Button btregister;
    EditText etusername,etpassword,etemail,etmobile;
    DatabaseReference mdatabaseref;
    String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        spinner=findViewById(R.id.spinner);
        btregister=findViewById(R.id.btsubmit);
        etusername=findViewById(R.id.etusername);
        etpassword=findViewById(R.id.etpassword);
        etemail=findViewById(R.id.etemail);
        etmobile=findViewById(R.id.etmobile);

        String users[]=new String[]{"--select--","USER","PG OWNER"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.spinner_row,users);
        spinner.setAdapter(arrayAdapter);

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etusername.getText().toString();
                String password = etpassword.getText().toString();
                String mobile = etmobile.getText().toString();
                String email = etemail.getText().toString();
                String usertype=spinner.getSelectedItem().toString();
                if (username == null || username.length() < 5) {
                    etusername.setError("username should be not less than 4 characters");
                } else if (password == null || password.length() < 6) {
                    etpassword.setError("password cannot be less than 6 characters");
                } else if (email == null || !(email.contains("@")) || !(email.contains(".com"))) {
                    etemail.setError("Email is not in correct format");

                } else if (mobile == null || mobile.length() < 10) {
                    etmobile.setError("Mobile number less than 10 numbers");
                } else if(usertype==null || usertype.equalsIgnoreCase("--select--")){
                    ToastHelper.toastMsg(getApplicationContext(),"select user type");
                }

                else {
                    createUser(username, password, mobile, email,usertype);
                }


            }
        });


    }

    private void createUser(String username, String password, String mobile, String email,String usertype) {

       mdatabaseref= FirebaseDatabase.getInstance().getReference("pgusers");


         userId = mdatabaseref.push().getKey();
        PgOwner user = new PgOwner(userId,username, password, email, mobile,usertype);
        mdatabaseref.child(userId).setValue(user);
        addUserChangeListener();

    }

    private void addUserChangeListener() {

        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("connecting...");
        progressDialog.show();
        mdatabaseref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PgOwner user = dataSnapshot.getValue(PgOwner.class);

                // Check for null
                if (user == null) {
                    Log.e("data", "User data is null!");
                    ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                    progressDialog.hide();
                    return;
                }else {

                    Log.e("data", "User data is changed!" + user.getMobile() + ", " + user.getEmail());
                    ToastHelper.toastMsg(getApplicationContext(), "Registration success");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    progressDialog.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("data","user data is cancelled");
                ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                progressDialog.hide();
            }
        });
    }


}
