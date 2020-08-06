package com.example.editquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.editquiz.modal.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;

public class MainActivity extends AppCompatActivity implements LocationListener {
    DatabaseReference mDatabase;
    Button start_button;
    EditText timer;
    long count;
    LocationManager locationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        getLocation();//storing location in DB of this app
        if(locationManager != null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Toast.makeText(this,""+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();
            //Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("EditQuizLoc").removeValue();
            mDatabase.child("EditQuizLoc").child("Latitude").setValue(location.getLatitude());
            mDatabase.child("EditQuizLoc").child("Longitude").setValue(location.getLongitude());
        }

        start_button = (Button) findViewById(R.id.start_quiz);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Questions").removeValue();
                mDatabase.child("students").removeValue();
                timer = (EditText) findViewById(R.id.time_count);
                try {
                    // checking valid integer using parseInt() method
                    Integer.parseInt(timer.getText().toString());
                    open_QuestionACT();
                    mDatabase.child("time").setValue(Integer.valueOf(timer.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Enter Correct time in mins", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void open_QuestionACT() {
        Intent intent = new Intent(this,Qustion_Entry.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {

    }

    @Override
    public void onLocationChanged(Location location) {

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

