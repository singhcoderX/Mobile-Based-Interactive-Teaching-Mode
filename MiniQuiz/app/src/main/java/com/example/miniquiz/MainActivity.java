package com.example.miniquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniquiz.modal.Question;
import com.example.miniquiz.modal.coordinate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements LocationListener {
    Button b1, b2, b3, b4;
    TextView t1_question, timerTxt;
    int total = 0;
    int correct = 0;
    int wrong = 0;
    DatabaseReference ref;
    DatabaseReference mDatabase;
    LocationManager locationManager;
    Location location;
    coordinate coord;
    boolean isBackground = false;
    boolean resultt = false;
    int restricton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restricton = 15;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }
        getLocation();//storing location in DB of this app
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Log.d(TAG, location == null ? "NO LastLocation" : location.toString());

            mDatabase = FirebaseDatabase.getInstance().getReference().child("EditQuizLoc");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        openHold();
                        return;
                    }
                    coord = dataSnapshot.getValue(com.example.miniquiz.modal.coordinate.class);
                    Log.d("TaG", "Latitude Of EditQUiz" + String.valueOf(coord.Latitude));
                    Log.d("TaG", "Longitude Of EditQUiz" + String.valueOf(coord.Longitude));
                    Log.d("TaG", "Latitude Of MiniQuiz" + String.valueOf(location.getLatitude()));
                    Log.d("TaG", "Longitude Of MiniQuiz" + String.valueOf(location.getLongitude()));

                    double distance = getdistance(location.getLatitude(), coord.Latitude, location.getLongitude(), coord.Longitude);
                    Log.d("TaG", "Distance = " + String.valueOf(distance));
                    if (distance > restricton) {
                        circumferenceRestriction(distance);
                    } else {
                        b1 = (Button) findViewById(R.id.button1);
                        b2 = (Button) findViewById(R.id.button2);
                        b3 = (Button) findViewById(R.id.button3);
                        b4 = (Button) findViewById(R.id.button4);

                        t1_question = (TextView) findViewById(R.id.questionTxt);
                        timerTxt = (TextView) findViewById(R.id.timerTxt);
                        updateQuestion();
                        DatabaseReference timer = FirebaseDatabase.getInstance().getReference().child("time");
                        timer.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    return;
                                }
                                int rev_timer = Integer.parseInt(dataSnapshot.getValue().toString());
                                reverseTimer(rev_timer * 60, timerTxt);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackground = true;
        }
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 100);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateQuestion() {
        total++;
        if(isBackground == true )
        {
            Toast.makeText(getApplicationContext(),"Your Quiz has ended Because u pressed Home button",Toast.LENGTH_SHORT).show();
            Intent myIntent  = new Intent(MainActivity.this,ResultActivity.class);
            myIntent.putExtra("total",String.valueOf(total-1));
            myIntent.putExtra("correct",String.valueOf(correct));
            myIntent.putExtra("incorrect",String.valueOf(wrong));
            resultt = true;
            Log.d("TAG","Result ACt1");
            startActivity(myIntent);
            return;
        }
        final long[] total_count = new long[1];
        DatabaseReference total_count_db = FirebaseDatabase.getInstance().getReference().child("Questions");
        total_count_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_count[0] = dataSnapshot.getChildrenCount();
                Log.d("TAG","Totoal count = "+total_count[0]);

                if(total_count[0] == 0)
                {
                    //no entries of Questions
                    Intent intent = new Intent(MainActivity.this,hold.class);
                    resultt = true;
                    startActivity(intent);
                    return;

                }
                Log.d("TAG","timestamp1");
                if(total > total_count[0]){
                    //open result activity
                    Intent myIntent  = new Intent(MainActivity.this,ResultActivity.class);
                    myIntent.putExtra("total",String.valueOf(total-1));
                    myIntent.putExtra("correct",String.valueOf(correct));
                    myIntent.putExtra("incorrect",String.valueOf(wrong));
                    resultt = true;
                    Log.d("TAG","Result ACt2");
                    startActivity(myIntent);


                }else
                {
                    Log.d("TAG","timestamp2");
                    ref = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(total));
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Question ques = dataSnapshot.getValue(Question.class);
                            t1_question.setText(ques.getQuestion());
                            b1.setText(ques.getOption1());
                            b2.setText(ques.getOption2());
                            b3.setText(ques.getOption3());
                            b4.setText(ques.getOption4());

                            b1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(b1.getText().toString().equals(ques.getAnswer()))
                                    {   b1.setBackgroundColor(Color.GREEN);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                correct++;
                                                b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                                updateQuestion();
                                            }
                                        },1500);
                                    }else
                                    {
                                        //answer is wrong so we need to find correct answer and make it green
                                        wrong ++;
                                        b1.setBackgroundColor(Color.RED);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                b1.setBackgroundColor(Color.parseColor("#03A9F4"));
                                                updateQuestion();
                                            }
                                        },1500);

                                    }
                                }
                            });
                            b2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(b2.getText().toString().equals(ques.getAnswer()))
                                    {   b2.setBackgroundColor(Color.GREEN);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                correct++;
                                                b2.setBackgroundColor(Color.parseColor("#03A9F4"));
                                                updateQuestion();
                                            }
                                        },1500);
                                    }else
                                    {
                                        //answer is wrong so we need to find correct answer and make it green
                                        wrong ++;
                                        b2.setBackgroundColor(Color.RED);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                b2.setBackgroundColor(Color.parseColor("#03A9F4"));

                                                updateQuestion();
                                            }
                                        },1500);

                                    }
                                }
                            });
                            b3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(b3.getText().toString().equals(ques.getAnswer()))
                                    {   b3.setBackgroundColor(Color.GREEN);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                correct++;
                                                b3.setBackgroundColor(Color.parseColor("#03A9F4"));
                                                updateQuestion();
                                            }
                                        },1500);
                                    }else
                                    {
                                        //answer is wrong so we need to find correct answer and make it green
                                        wrong ++;
                                        b3.setBackgroundColor(Color.RED);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                b3.setBackgroundColor(Color.parseColor("#03A9F4"));

                                                updateQuestion();
                                            }
                                        },1500);

                                    }
                                }
                            });
                            b4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(b4.getText().toString().equals(ques.getAnswer()))
                                    {   b4.setBackgroundColor(Color.GREEN);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                correct++;
                                                b4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                                updateQuestion();
                                            }
                                        },1500);
                                    }else
                                    {
                                        //answer is wrong so we need to find correct answer and make it green
                                        wrong ++;
                                        b4.setBackgroundColor(Color.RED);

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                b4.setBackgroundColor(Color.parseColor("#03A9F4"));
                                                updateQuestion();
                                            }
                                        },1500);

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void reverseTimer(int Seconds,final TextView tv){
        new CountDownTimer(Seconds *  1000 +1000 , 1000)
        {
            public void onTick(long milliUtilFinished)
            {
                int seconds = (int) (milliUtilFinished / 1000);
                int minutes = seconds / 60;
                 seconds = seconds%60;
                 tv.setText(String.format("%02d",minutes)+
                         ":"+String.format("%02d",seconds));
            }
            public void onFinish(){
                tv.setText("Finished");
                if(resultt == false) {
                    Intent myIntent = new Intent(MainActivity.this, ResultActivity.class);
                    myIntent.putExtra("total", String.valueOf(total));
                    myIntent.putExtra("correct", String.valueOf(correct));
                    myIntent.putExtra("incorrect", String.valueOf(wrong));

                    startActivity(myIntent);
                }
            }
        }.start();
    }
    public double getdistance(double lat1,
                              double lat2, double lon1,
                              double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371000;//for meters

        // calculate the result
        return(c * r);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this,"Location Changed"+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();
        if(coord != null) {
            double distance = getdistance(location.getLatitude(), coord.Latitude, location.getLongitude(), coord.Longitude);
            Log.d("TaG", "Distance = " + String.valueOf(distance));
            if (distance > restricton) {
                circumferenceRestriction(distance);
            }
        }
    }
    public void circumferenceRestriction(double distance){
        Toast.makeText(this,"You Are out Of Circumference:"+String.valueOf(distance-restricton)+" meters",Toast.LENGTH_SHORT).show();
       openHold();
    }
public void openHold(){
    Intent intent = new Intent(MainActivity.this,hold.class);
    startActivity(intent);
}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
