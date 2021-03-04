package com.guru.pglocator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.Messages;
import com.guru.pglocator.models.PgDetails;
import com.guru.pglocator.models.PgOwner;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;

import java.util.HashMap;

public class ViewProfile  extends AppCompatActivity {

    EditText etusername,etpassword,etemail,etmobile,etusertype;
    Button btsubmit;
    DatabaseReference mdatabaseref;
    HashMap map;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        etusername=findViewById(R.id.etusername);
        etpassword=findViewById(R.id.etpassword);
        etemail=findViewById(R.id.etemail);
        etmobile=findViewById(R.id.etmobile);
        btsubmit=findViewById(R.id.btsubmit);
        etusertype=findViewById(R.id.etusertype);
        map= Store.getUserDetails(this);
        mdatabaseref= FirebaseDatabase.getInstance().getReference("pgusers");

        progressDialog = new ProgressDialog(ViewProfile.this);
                progressDialog.setTitle("connecting...");
                progressDialog.show();
                mdatabaseref.child(map.get("username").toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        PgOwner user = dataSnapshot.getValue(PgOwner.class);

                        // Check for null
                        if (user == null) {
                            Log.e("data", "User data is null!");
                            ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                            progressDialog.hide();
                            return;
                        }

                        Log.e("data", "User data is changed!" + user.getMobile() + ", " + user.getEmail());
                       etusertype.setText(user.getUsertype());
                       etusername.setText(user.getUsername());
                       etemail.setText(user.getEmail());etmobile.setText(user.getMobile());
                        etpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etpassword.setSelection(etpassword.length());
                       etpassword.setText(user.getPassword());
                       progressDialog.hide();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("data","user data is cancelled");
                        ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                        progressDialog.hide();
                    }
                });


btsubmit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDialogUpdate(ViewProfile.this, etpassword.getText().toString());
    }
});


    }

    private void showDialogUpdate(Context context, final String oldpass) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_change_password);
        dialog.setTitle("change password");
     final EditText etoldpassword=dialog.findViewById(R.id.etoldpassword);
     final EditText etnewpassword=dialog.findViewById(R.id.etnewpassword);
     final EditText etrepassword=dialog.findViewById(R.id.etrepassword);
     Button btsubmit=dialog.findViewById(R.id.btsubmit);
     Button btcancel=dialog.findViewById(R.id.btcancel);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String oldpassword=etoldpassword.getText().toString();
               final String newpassword=etnewpassword.getText().toString();
               String repassword=etrepassword.getText().toString();

               if(!(oldpassword.equalsIgnoreCase(oldpass))){
                   ToastHelper.toastMsg(getApplicationContext(),"Old password is wrong");
               }else if(!(newpassword.equalsIgnoreCase(repassword))){
                   ToastHelper.toastMsg(getApplicationContext(),"New password and Re-password does not match");
               }else{



                   mdatabaseref=FirebaseDatabase.getInstance().getReference("pgusers");
                   mdatabaseref.child(map.get("username").toString().trim()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         /*  PgDetails pgDetails1=dataSnapshot.getValue(PgDetails.class);*/
                           Log.e("username in profile",map.get("username").toString());
                           PgOwner user = new PgOwner(map.get("username").toString().trim(),etusername.getText().toString(),newpassword,etemail.getText().toString(),etmobile.getText().toString(),etusertype.getText().toString());
                           mdatabaseref.child(map.get("username").toString().trim()).setValue(user);
                           ToastHelper.toastMsg(getApplicationContext(),"updated password");

                           dialog.dismiss();
                           // customAdapter.notifyDataSetChanged();

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                           ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                       }
                   });


                  /* PgOwner userq = new PgOwner(map.get("username").toString().trim(),etusername.getText().toString(),etpassword.getText().toString(),etemail.getText().toString(),etmobile.getText().toString(),etusertype.getText().toString());
                   mdatabaseref.child("-LYBfuUFqn9V7xITFF18").setValue(userq);
                   //addUserChangeListener();
                  // progressDialog.dismiss();
                   dialog.hide();*/
               }





            }
        });

        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();





    }
}
