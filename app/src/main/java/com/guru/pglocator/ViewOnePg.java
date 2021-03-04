package com.guru.pglocator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ViewOnePg  extends AppCompatActivity  {

    ImageView imageView;
    TextView tvhostelname,tvfaci,tvcontact;
    Button btroute,btmessage;
    PgDetails pgDetails;
    String messageid;
    DatabaseReference mdatabaseref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_pg);
       pgDetails = (PgDetails) getIntent().getSerializableExtra("obj");
       Log.e("details",pgDetails.getContact());
       tvhostelname=findViewById(R.id.tvhostelname);
       tvfaci=findViewById(R.id.tvfaci);
       tvcontact=findViewById(R.id.tvcontact);
       imageView=findViewById(R.id.imageView);
       btmessage=findViewById(R.id.btmessage);
        Picasso.get().load(pgDetails.getImgpath()).into(imageView);
        tvhostelname.setText(pgDetails.getHostelname());
        tvfaci.setText(pgDetails.getFaci());
        tvcontact.setText(pgDetails.getContact());
        btroute=findViewById(R.id.btroute);
        btmessage=findViewById(R.id.btmessage);
        btroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ViewHostelMap.class);
                i.putExtra("location",pgDetails.getLocation());
                startActivity(i);
            }
        });

        btmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate(ViewOnePg.this, pgDetails);

            }
        });
    }

    private void showDialogUpdate(Context context, final PgDetails pgDetails) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_send_message);
        dialog.setTitle("Update"+pgDetails.getKey());
        final EditText etmessage=dialog.findViewById(R.id.etmessage);
        Button btsubmit=dialog.findViewById(R.id.btsubmit);
        Button btcancel=dialog.findViewById(R.id.btcancel);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messages=etmessage.getText().toString();
                String pgkey=pgDetails.getKey();
                HashMap map= Store.getUserDetails(getApplicationContext());

                 mdatabaseref= FirebaseDatabase.getInstance().getReference("messages");
                
                messageid=mdatabaseref.push().getKey();
                Messages messages1=new Messages(map.get("username").toString(),pgkey,messages,pgDetails.getHostelname());
                messages1.setUsername(map.get("name").toString().trim());
                messages1.setType(map.get("type").toString().trim());
                mdatabaseref.child(messageid).setValue(messages1);
                addMessageChangeListener();

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
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.5);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    private void addMessageChangeListener() {


        final ProgressDialog progressDialog = new ProgressDialog(ViewOnePg.this);
        progressDialog.setTitle("connecting...");
        progressDialog.show();
        mdatabaseref.child(messageid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Messages user = dataSnapshot.getValue(Messages.class);

                // Check for null
                if (user == null) {
                    Log.e("data", "User data is null!");
                    ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                    progressDialog.hide();
                    return;
                }


                ToastHelper.toastMsg(getApplicationContext(),"Sent Message successfully");
                startActivity(new Intent(getApplicationContext(),UserSuccess.class));
                progressDialog.hide();
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
