package com.guru.pglocator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.guru.pglocator.models.Messages;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public  class MessageList  extends AppCompatActivity {

    ListView lvlsit;
    List<Messages> messageViewList;
    DatabaseReference mdatabaseref;
 MessagesAdapter messagesAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        lvlsit=findViewById(R.id.lvlist);
        messageViewList=new ArrayList<>();

        mdatabaseref= FirebaseDatabase.getInstance().getReference("messages");
        HashMap map= Store.getUserDetails(this);
        Query query=mdatabaseref.orderByChild(map.get("username").toString().trim());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot message : dataSnapshot.getChildren()) {
                        Messages msg=message.getValue(Messages.class);
                        Log.e("msg",msg.getMessage());
                        messageViewList.add(msg);
                    }
                    messagesAdapter=new MessagesAdapter(MessageList.this,messageViewList);
                    lvlsit.setAdapter(messagesAdapter);
             messagesAdapter.notifyDataSetChanged();
                }else{
                    ToastHelper.toastMsg(getApplicationContext(),"no msgs found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
