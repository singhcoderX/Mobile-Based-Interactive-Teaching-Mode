package com.example.editquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
// ...
    Button start_button;
    EditText timer ;
    long count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_button = (Button) findViewById(R.id.start_quiz);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Questions").removeValue();
                mDatabase.child("students").removeValue();
                timer = (EditText) findViewById(R.id.time_count);
                try
                {
                    // checking valid integer using parseInt() method
                    Integer.parseInt(timer.getText().toString());
                    open_QuestionACT();
                    mDatabase.child("time").setValue(Integer.valueOf(timer.getText().toString()));
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(),"Enter Correct time in mins",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    private void open_QuestionACT() {
        Intent intent = new Intent(this,Qustion_Entry.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {

    }
}

