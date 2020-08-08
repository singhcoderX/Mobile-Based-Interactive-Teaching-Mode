package com.example.miniquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniquiz.modal.Resultt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

public class ResultActivity extends AppCompatActivity {
    TextView t1,t2,t3;
    Button get_result_b;
    EditText RollNO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        t1 = (TextView)findViewById(R.id.textView4);
        t2 = (TextView)findViewById(R.id.textView5);
        t3 = (TextView)findViewById(R.id.textView6);
        Intent i = getIntent();
        final String question = i.getStringExtra("total");
        final String correct = i.getStringExtra("correct");
        final String wrong = i.getStringExtra("incorrect");
        get_result_b = (Button)findViewById(R.id.get_result);
        get_result_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollNO = (EditText)findViewById(R.id.roll_no);
                String rollno = RollNO.getText().toString();
                if(rollno.matches("")|| !check_roll_no(rollno))
                {

                    Toast.makeText(getApplicationContext(),"Enter Roll NO/Check it",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    RollNO.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Score uploaded",Toast.LENGTH_SHORT).show();
                }
               DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("students").child(rollno.toString());
                Map<String,Object> taskMap = new HashMap<>();
                taskMap.put("RollNo",rollno);
                taskMap.put("TOTAL", question);
                taskMap.put("CORRECT", correct);
                taskMap.put("WRONG", wrong);

                reff.setValue(taskMap);
                t1.setText(question);
                t2.setText(correct);
                t3.setText(wrong);
            }
        });

    }

    private boolean check_roll_no(String rollno) {
        char[] charArray = rollno.toCharArray();
        if(rollno.length()>9)
        {
            return false;
        }
        if(     charArray[0] != 'B' ||
                charArray[1] != '1' ||
                charArray[2] != '7' ||
                charArray[3] != 'C' ||
                charArray[4] != 'S' )
        {return false;}
        String roll = rollno.substring(5,8);
        try {
            Integer.parseInt(roll);
        }catch (NumberFormatException e)
        {
            Toast.makeText(getApplicationContext(),"Wrong INPUT in ANSWER Field",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {

    }
}
