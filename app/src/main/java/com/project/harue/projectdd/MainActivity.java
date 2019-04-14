package com.project.harue.projectdd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.harue.projectdd.Adapter.ImageAdapter;
import com.project.harue.projectdd.Model.Contener;
import com.project.harue.projectdd.Model.Users;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView addimage;

    List<Contener> imageList;
    List<Users> mUser;

    ImageAdapter imageAdapter;

    ImageView selectimage;

    FirebaseAuth mAuth ;
    FirebaseUser curUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("หน้าหลัก");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        addimage = findViewById(R.id.addRy);
        selectimage = findViewById(R.id.selectimage);
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, page3.class));
            }
        });

        addimage.setHasFixedSize(true);
        addimage.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();
        creat_post();


    }

    private void  creat_post(){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listchild");


        imageList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               imageList.clear();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Contener contener = postsnap.getValue(Contener.class);
                    if(contener.getUserid().equals(curUser.getUid())){

                        imageList.add(contener);

                    }



                }

                imageAdapter = new ImageAdapter(getApplicationContext(), imageList,MainActivity.this);
                addimage.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.signout, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.signoutid:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
