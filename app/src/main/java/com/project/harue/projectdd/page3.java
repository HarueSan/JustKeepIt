package com.project.harue.projectdd;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.harue.projectdd.Model.Contener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class page3 extends AppCompatActivity {

    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;

    private Uri pickedImgUri = null;

    ImageView addimage;
    EditText namename;
    EditText price;
    TextView dateStart;
    TextView dateStop;

    Button save;

    DatePickerDialog.OnDateSetListener mdate;

    String dateid;
    String curtimeid;

    FirebaseAuth mAuth;
    FirebaseUser curUser;

    String imageDownlaodLink;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        getSupportActionBar().hide();

        addimage = findViewById(R.id.addimage);
        namename = findViewById(R.id.nameprice);
        price = findViewById(R.id.priceprice);
        save = findViewById(R.id.savedata);
        dateStart = findViewById(R.id.curtime);
        dateStop = findViewById(R.id.stoptime);
        progressBar = findViewById(R.id.progress_bar);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(currentTime);
        Log.e("tag", currentDateandTime);

        curtimeid = currentDateandTime;

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        dateStart.setText("Start: " + currentDateandTime);
        dateStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(page3.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mdate,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //
                month += 1;
                String new_mount;
                if (month < 10) {
                    new_mount = "0" + month;
                } else {
                    new_mount = ""+ month;
                }
                dateStop.setText("Stop: " + dayOfMonth + "/" + new_mount + "/" + year);

                dateid = dateStop.getText().toString();
            }
        };

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!namename.getText().toString().equals("") && !price.getText().toString().equals("") && pickedImgUri != null) {
                    addimagefirebase();
                    save.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(),"กรุณาใส่ชื่อและราคาของสินค้า",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();

            }
        });
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getApplicationContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            // everything goes well : we have permission to access user gallery
            openGallery();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }



    // when user picked an image ...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            addimage.setImageURI(pickedImgUri);

        }


    }

    private void addimagefirebase() {

        if (!namename.getText().toString().equals("") && !price.getText().toString().equals("") && pickedImgUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("list");
            final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageDownlaodLink = uri.toString();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("list1");

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Contener contener = new Contener(imageDownlaodLink,
                                            namename.getText().toString(),
                                            price.getText().toString(),
                                            curtimeid,
                                            dateid.replace("Stop: ", "")
                                    ,curUser.getUid(),0);

                                    tofirebase(contener);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    private void tofirebase(Contener contener) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("listchild").push();

        final String key = myRef.getKey();
        contener.setPostid(key);




        myRef.setValue(contener).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Intent intent = new Intent(getApplicationContext(), SubHomeActivity.class);
                intent.putExtra("priceid", price.getText().toString());
                intent.putExtra("dateid", dateid);
                intent.putExtra("curdateid", curtimeid);
                intent.putExtra("imgurl",imageDownlaodLink);
                intent.putExtra("postid",key);
                startActivity(intent);


                finish();
            }
        });
    }

}
