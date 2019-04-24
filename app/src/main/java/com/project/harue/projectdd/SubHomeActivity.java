package com.project.harue.projectdd;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import maes.tech.intentanim.CustomIntent; //fade

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.harue.projectdd.Model.Contener;
import com.r0adkll.slidr.Slidr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SubHomeActivity extends AppCompatActivity {

    String price;
    String datee;
    String curdate;
    String imgurl;
    String postid;

    long currentPrice;

    TextView getprice;
    TextView gettime;
    TextView balance;

    long cal = 0;

    ImageView imageView;
    RelativeLayout aomtang, backtohome;
    Dialog dialog;
    EditText savetang;
    Button btn_done, btn_cancel;
    TextView priceproduct;

    String get_savetang = " ";
    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_home);
        Slidr.attach(this);

//        Hide Actionbar
        getSupportActionBar().hide();

        getprice = findViewById(R.id.price);
        gettime = findViewById(R.id.time);
        balance = findViewById(R.id.balance);

        imageView = findViewById(R.id.img);
        aomtang = findViewById(R.id.aomtang);
        backtohome = findViewById(R.id.backtohome);
        priceproduct = findViewById(R.id.priceproduct);




        Intent intent = getIntent();
        price = intent.getStringExtra("priceid");
        datee = intent.getStringExtra("dateid");
        curdate = intent.getStringExtra("curdateid");
        imgurl = intent.getStringExtra("imgurl");
        postid = intent.getStringExtra("postid");






        ref = FirebaseDatabase.getInstance().getReference("listchild").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contener contener = dataSnapshot.getValue(Contener.class);
                try {
                    priceproduct.setText("ราคาสินค้า: " + contener.getPrice_object());
                    long remainingBalance = Long.parseLong(contener.getPrice_object()) - contener.getStartPrice();
                    if (remainingBalance < 0) {
                        remainingBalance = 0 ;
                    }
                    balance.setText(Long.toString(remainingBalance));
                } catch (Exception e) {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        datee = datee.replace("/", "");
      datee = datee.replace("Stop: ", "");
//        curdate = curdate.replace("/", "");

        try {
            Calendar currentDate = Calendar.getInstance();
            Calendar objectiveDate = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            objectiveDate.setTime(sdf.parse(datee));

            this.cal = this.daysBetween(currentDate,objectiveDate);
//Logging for data testing
            Log.i("TEST TEST",currentDate.getTime().toString());
            Log.i("TEST TEST",objectiveDate.getTime().toString());
            Log.i("TEST TEST",datee);
            Log.i("TEST TEST",Long.toString(this.cal));

            if(currentDate.getTimeInMillis() > objectiveDate.getTimeInMillis()) {
                this.cal = -this.cal;

            }




        } catch (Exception e) {

        }


        gettime.setText(this.cal + " วัน");




        calculateprice(Integer.parseInt(price));


        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("listchild").child(postid);

        Glide.with(getApplicationContext()).load(imgurl).into(imageView);
        aomtang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(SubHomeActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_activity);

                savetang = dialog.findViewById(R.id.savetang);
                btn_done = dialog.findViewById(R.id.btn_done);
                btn_cancel = dialog.findViewById(R.id.btn_cancel);

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!savetang.getText().toString().equals("")) {
                            get_savetang = savetang.getText().toString();
                            getprice.setText("" + (Integer.parseInt(getprice.getText().toString()) +
                                    Integer.parseInt(get_savetang)));



                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("startPrice",Integer.parseInt(getprice.getText().toString()));
                            ref.updateChildren(hashMap);


                            dialog.dismiss();

                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contener contener = dataSnapshot.getValue(Contener.class);
            try {

                getprice.setText("" + contener.getStartPrice());

                if (contener.getStartPrice() >= Integer.parseInt(contener.getPrice_object())) {
                    new AlertDialog.Builder(SubHomeActivity.this)
                            .setTitle("ขอแสดงความยินดีด้วย")
                            .setMessage("คุณเก็บเงินซื้อ " + contener.getName_object() + " ได้แล้ว")
                            .setPositiveButton("ตกลง", null)
                            .setNegativeButton(null, null)
                            .show();
                    aomtang.setEnabled(false);
                }
            } catch (Exception e){

            }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubHomeActivity.this,MainActivity.class));
                finish();
                CustomIntent.customType(SubHomeActivity.this, "fadein-to-fadeout");
            }
        });


    }
    // func for calculate price
    private void calculateprice(int price) {

    }
    //count days
    private long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

}
