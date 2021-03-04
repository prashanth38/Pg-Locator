package com.guru.pglocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.Messages;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;

public class UserSuccess extends AppCompatActivity {

    Button btview,btmessage;

    DatabaseReference mdatabaseref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_success);
        btview=findViewById(R.id.btview);
        btmessage=findViewById(R.id.btmessage);
        btview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ViewUserPg.class));
            }
        });

        btmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MessageList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.profile:{

                Intent i=new Intent(getApplicationContext(),ViewProfile.class);
                startActivity(i);


                break;
            }
            case R.id.logout:{

               startActivity(new Intent(this,MainActivity.class));
                Store.logout(this);
               finish();

            }

        }

        return super.onOptionsItemSelected(item);
    }
}
