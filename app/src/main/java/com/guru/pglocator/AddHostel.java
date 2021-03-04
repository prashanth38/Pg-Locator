package com.guru.pglocator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guru.pglocator.models.PgDetails;
import com.guru.pglocator.models.PgOwner;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddHostel  extends AppCompatActivity {

    ImageView imageView,imgView;
    EditText etlocation,ethostelname,etfaci,etcontact;
    final int REQUEST_GALLERY_CODE=999;
    final int REQUEST_LOCATION_CODE=998;
    StorageReference storageReference;
    DatabaseReference mDatabase;
    String userId;
    Button btadd;
    String location;
    String imagepath; private Uri filePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hostel);
        imageView=findViewById(R.id.map);
        imgView=findViewById(R.id.imgview);
        etlocation=findViewById(R.id.emap);
        ethostelname=findViewById(R.id.ethostelname);
        etcontact=findViewById(R.id.etcontact);
        etfaci=findViewById(R.id.etfaci);
        btadd=findViewById(R.id.btadd);
        storageReference= FirebaseStorage.getInstance().getReference();

        ActivityCompat.requestPermissions(AddHostel.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION_CODE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddHostel.this,MapsActivity.class);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2

            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddHostel.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_GALLERY_CODE);

            }
        });

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostelname=ethostelname.getText().toString();
                String faci=etfaci.getText().toString();
                String location=etlocation.getText().toString();
                String contact=etcontact.getText().toString();
                HashMap map=Store.getUserDetails(getApplicationContext());
                Log.e("user key is",map.get("username").toString());
                mDatabase= FirebaseDatabase.getInstance().getReference("hostels");

                 userId = mDatabase.push().getKey();
                PgDetails user = new PgDetails(map.get("username").toString(),hostelname,faci,contact,imagepath,location);
                mDatabase.child(userId).setValue(user);
                addUserChangeListener();



            }
        });

    }

    private void addUserChangeListener() {


        final ProgressDialog progressDialog = new ProgressDialog(AddHostel.this);
        progressDialog.setTitle("connecting...");
        progressDialog.show();
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
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
                ToastHelper.toastMsg(getApplicationContext(),"Registration success");
                startActivity(new Intent(getApplicationContext(),PgOwnerSuccess.class));
                finish();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
           etlocation.setText(message);
        }

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data!=null){

            Uri uri= data.getData();
            filePath=data.getData();
            uploadImage();

          /*  try {
                InputStream inputStream= getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                imgView.setImageBitmap(scaled);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }else{

        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage() {


        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddHostel.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        /*final UploadTask.TaskSnapshot downloadUri = task.getResult();*/
                        final String[] URL = new String[1];

                        if(task.isSuccessful()) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    URL[0] = uri.toString();

                                   imagepath=URL[0];

                                }
                            });
                        }




                    } else {
                        Log.i("wentWrong","downloadUri failure");
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddHostel.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,int[] grantResults) {

        if(requestCode==REQUEST_GALLERY_CODE){

            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_GALLERY_CODE);

            }else{

                Toast.makeText(getApplicationContext(),"you do not have permissions ",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if(requestCode==REQUEST_LOCATION_CODE){

            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


            }else{

                Toast.makeText(getApplicationContext(),"you do not have permissions ",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

   