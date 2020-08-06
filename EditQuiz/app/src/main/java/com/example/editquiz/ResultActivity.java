package com.example.editquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.editquiz.modal.Values;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ListView mListView;
    ArrayList<String>arrayList = new ArrayList<>();
    ArrayAdapter<String>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","Inside Result Activity");
        setContentView(R.layout.activity_result);
        mListView = (ListView)findViewById(R.id.list_view);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        mListView.setAdapter(adapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG","Inside Firebase fn");
                DataSnapshot contactSnapshot = dataSnapshot.child("students");
                Iterable<DataSnapshot> stud = contactSnapshot.getChildren();
                for (DataSnapshot contact : stud) {
                    Values c = contact.getValue(Values.class);

                    String str = c.to_String();
                    Log.d("TAG","contact:: "+ str);
                    arrayList.add(str);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed()
    {

    }

}
