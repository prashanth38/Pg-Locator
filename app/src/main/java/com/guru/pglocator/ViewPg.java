package com.guru.pglocator;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.guru.pglocator.utils.CustomAdapter;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ViewPg  extends AppCompatActivity {


    ListView listView;
    private DatabaseReference mdatabaseRef;
    List<PgDetails> pgDetailsList;
    final int REQUEST_GALLERY_CODE=999;
    final int REQUEST_LOCATION_CODE=998;
    StorageReference storageReference;
    DatabaseReference mDatabase;
    String userId; String location;
    String imagepath; private Uri filePath;
    HashMap map;
    CustomAdapter customAdapter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pg);
        listView=findViewById(R.id.lvlist);
        pgDetailsList=new ArrayList<>();
        customAdapter=new CustomAdapter(this,R.layout.hostels_row,pgDetailsList);
        listView.setAdapter(customAdapter);
         map= Store.getUserDetails(getApplicationContext());
        storageReference= FirebaseStorage.getInstance().getReference();

        mdatabaseRef= FirebaseDatabase.getInstance().getReference("hostels");

        Query query=mdatabaseRef.orderByChild("id").equalTo(map.get("username").toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot hostel:dataSnapshot.getChildren()){
                        String key=hostel.getKey();
                        PgDetails pgDetails=hostel.getValue(PgDetails.class);
                        pgDetails.setKey(key);
                        pgDetailsList.add(pgDetails);
                    }
              customAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
            }
        });




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewPg.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                           PgDetails pgDetails=pgDetailsList.get(position);
                             showDialogUpdate(ViewPg.this, pgDetails);

                        } else {
                            PgDetails pgDetails=pgDetailsList.get(position);
                             showDialogDelete(pgDetails);
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });








    }

    private void showDialogDelete(final PgDetails pgDetails) {


        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ViewPg.this);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                  //  sqliteDb.deleteData(idFood);

                    mdatabaseRef=FirebaseDatabase.getInstance().getReference("hostels");
                    mdatabaseRef.child(pgDetails.getKey()).removeValue();

                    Toast.makeText(getApplicationContext(), "Delete successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),PgOwnerSuccess.class);
                   
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateHostelList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateHostelList() {



        mdatabaseRef= FirebaseDatabase.getInstance().getReference("hostels");

        Query query=mdatabaseRef.orderByChild("id").equalTo(map.get("username").toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot hostel:dataSnapshot.getChildren()){
                        String key=hostel.getKey();
                        PgDetails pgDetails=hostel.getValue(PgDetails.class);
                        pgDetails.setKey(key);
                        pgDetailsList.add(pgDetails);
                    }
                    customAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
            }
        });



    }

    private void showDialogUpdate(ViewPg activity, final PgDetails pgDetails) {


        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_add_hostel);
        dialog.setTitle("Update"+pgDetails.getKey());

        final EditText ethostel=dialog.findViewById(R.id.ethostelname);
        final EditText etfaci=dialog.findViewById(R.id.etfaci);
        EditText etmap=dialog.findViewById(R.id.emap);
        final EditText etcontact=dialog.findViewById(R.id.etcontact);
        ImageView imageView=dialog.findViewById(R.id.imgview);
        ImageView imgagemap=dialog.findViewById(R.id.map);
        Button btadd=dialog.findViewById(R.id.btadd);



        ethostel.setText(pgDetails.getHostelname());
       etfaci.setText(pgDetails.getFaci());
       etmap.setText(pgDetails.getLocation());
       etcontact.setText(pgDetails.getContact());
       location=etmap.getText().toString();
       imagepath=pgDetails.getImgpath();
        Picasso.get().load(pgDetails.getImgpath()).into(imageView);





        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.9);
        dialog.getWindow().setLayout(width, height);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ViewPg.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_GALLERY_CODE);
            }
        });



        imgagemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewPg.this,MapsActivity.class);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2
            }
        });

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mdatabaseRef=FirebaseDatabase.getInstance().getReference("hostels");
                mdatabaseRef.child(pgDetails.getKey().trim()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PgDetails pgDetails1=dataSnapshot.getValue(PgDetails.class);
                        PgDetails user = new PgDetails(map.get("username").toString(),ethostel.getText().toString(),etfaci.getText().toString(),etcontact.getText().toString(),imagepath,location);
                        mdatabaseRef.child(pgDetails.getKey()).setValue(user);
                        ToastHelper.toastMsg(getApplicationContext(),"updated PG DETAILS");

                        dialog.dismiss();
                       // customAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

ToastHelper.toastMsg(getApplicationContext(),"something went wrong");
                    }
                });


            }
        });

        dialog.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
            location=message;
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
                            Toast.makeText(ViewPg.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ViewPg.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
