package com.guru.pglocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.guru.pglocator.utils.Store;

public class PgOwnerSuccess extends AppCompatActivity {

    Button btaddhostel,btviewhostel,btviewpg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_owner_success);
        btaddhostel=findViewById(R.id.btaddhostel);
        btviewhostel=findViewById(R.id.viewhostel);
        btviewpg=findViewById(R.id.viewuser);



        btaddhostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddHostel.class));
            }
        });

        btviewhostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ViewPg.class));
            }
        });

        btviewpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MessageList.class));
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
