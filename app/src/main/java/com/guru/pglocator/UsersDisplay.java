package com.guru.pglocator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.PgOwner;
import com.guru.pglocator.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class UsersDisplay extends AppCompatActivity {

    ListView lvlist;
   List<PgOwner> pgOwnerList;
   String usertype;
   DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_display);
        lvlist=findViewById(R.id.lvlist);
        Bundle bundle=getIntent().getExtras();
        usertype=bundle.getString("usertype");
        pgOwnerList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("pgusers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    PgOwner pgOwner=dataSnapshot1.getValue(PgOwner.class);
                    pgOwnerList.add(pgOwner);
                }

                UsersAdapter usersAdapter=new UsersAdapter(UsersDisplay.this,pgOwnerList,usertype);
                lvlist.setAdapter(usersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
