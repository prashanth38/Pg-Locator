package com.guru.pglocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.PgOwner;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Button btreg,btsubmit,btadmin;
   EditText etusername,etpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etusername=findViewById(R.id.etusername);
        etpassword=findViewById(R.id.etpassword);
        btsubmit=findViewById(R.id.btsubmit);
        btadmin=findViewById(R.id.btadmin);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseInstance.getReference("app_title").setValue("pglocator");
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("appname", "App title updated");
                String appTitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("appname", "Failed to read app title value.", error.toException());
            }
        });

        btreg=findViewById(R.id.btregister);
        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        btadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminLogin.class));
                finish();
            }
        });


        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=etusername.getText().toString();
                final String password=etpassword.getText().toString();
                if(username.isEmpty() || username==null){
                    etusername.setError("Mail id cannot be empty");
                }else if(password.isEmpty() || password==null){
                    etpassword.setError("password cannot be empty");
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("connecting...");
                    progressDialog.show();
                    //firebase
                    mFirebaseDatabase=FirebaseDatabase.getInstance().getReference("pgusers");
                    Query query=mFirebaseDatabase.orderByChild("email").equalTo(username.trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){


                                for(DataSnapshot user:dataSnapshot.getChildren()){

                                    PgOwner user1=user.getValue(PgOwner.class);
                                    if(user1.getPassword().equalsIgnoreCase(password)){
                                        String key = user.getKey();
                                        if(user1.getUsertype().equalsIgnoreCase("user")){
                                            startActivity(new Intent(getApplicationContext(), UserSuccess.class));
                                            Store.userDetails(getApplicationContext(), key, user1.getUsername(), user1.getUsertype());
                                            finish();
                                            progressDialog.dismiss();

                                        }else if(user1.getUsertype().equalsIgnoreCase("PG OWNER")) {
                                            startActivity(new Intent(getApplicationContext(), PgOwnerSuccess.class));
                                            Store.userDetails(getApplicationContext(), key, user1.getUsername(), user1.getUsertype());
                                            progressDialog.dismiss();
                                        }else{
                                            ToastHelper.toastMsg(getApplicationContext(),"No user foundddddddddddddd");
                                            progressDialog.dismiss();
                                        }

                                    }else{
                                        ToastHelper.toastMsg(getApplicationContext(),"password is wrong");
                                        progressDialog.dismiss();
                                    }


                                }


                            }else{

                                ToastHelper.toastMsg(getApplicationContext(),"No user found");
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                        }
                    });



                }



            }
        });

    }
}
