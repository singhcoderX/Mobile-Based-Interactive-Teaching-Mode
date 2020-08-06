package com.example.editquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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

public class Qustion_Entry extends AppCompatActivity {
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qustion__entry);
        final LoadingDialog loadingDialog = new LoadingDialog(Qustion_Entry.this);
        Button b_submit = (Button)findViewById(R.id.submit);

        final ArrayList<Question> Questions_list = new ArrayList<Question>();

        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Q_Question = (EditText)findViewById(R.id.E_question);
                EditText Q_op1 = (EditText)findViewById(R.id.E_option1);
                EditText Q_op2 = (EditText)findViewById(R.id.E_option2);
                EditText Q_op3 = (EditText)findViewById(R.id.E_option3);
                EditText Q_op4 = (EditText)findViewById(R.id.E_option4);
                EditText Q_ans = (EditText)findViewById(R.id.E_answer);

                String S_Question = Q_Question.getText().toString();
                String S_option1 = Q_op1.getText().toString();
                String S_option2 = Q_op2.getText().toString();
                String S_option3 = Q_op3.getText().toString();
                String S_option4 = Q_op4.getText().toString();
                String x = Q_ans.getText().toString() ;
                if (    S_Question.matches("")  ||
                        S_option1.matches("")   ||
                        S_option2.matches("")   ||
                        S_option3.matches("")   ||
                        S_option4.matches("")   ||
                        x.matches("")
                ) {
                    Toast.makeText(getApplicationContext(),"Some Fields are empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                try
                {
                    // checking valid integer using parseInt() method
                    Integer.parseInt(x);
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(),"Wrong INPUT in ANSWER Field",Toast.LENGTH_SHORT).show();
                    return;
                }
                int S_answer = Integer.valueOf(x);
                if(S_answer >4)
                {
                    Toast.makeText(getApplicationContext(),"ANWER should be <= 4 only",Toast.LENGTH_SHORT).show();
                    return;
                }

                String answer;
                if(S_answer == 1){
                    answer = S_option1;
                }else if(S_answer == 2)
                {
                    answer = S_option2;
                }else if(S_answer == 3)
                {
                    answer = S_option3;
                }else
                {
                    answer = S_option4;
                }


                Question Q = new Question(S_Question,S_option1,S_option2,S_option3,S_option4,answer);
                Questions_list.add(Q);
                Q_ans.getText().clear();
                Q_Question.getText().clear();
                Q_op1.getText().clear();
                Q_op2.getText().clear();
                Q_op3.getText().clear();
                Q_op4.getText().clear();
            }
        });

        Button exit_button = (Button) findViewById(R.id.exit);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Questions");
                //Storing Timer for Quiz
                DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("time");
                //Storing Questions
                for(int i = 0;i<Questions_list.size();i++)
                {
                    mDatabase.child(String.valueOf(i+1)).setValue(Questions_list.get(i));
                }
                mdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int timer = dataSnapshot.getValue(Integer.class);
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                open_mainACT();
                            }
                        },(timer+1)*60000);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    private void open_mainACT() {
        Intent intent = new Intent(this,ResultActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {

    }
}
