package com.example.swadheen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class BasicQuestions extends AppCompatActivity {
    String type="";
    TextView Questions,QuestionNumber,Time;
    FirebaseAuth mAuth;
    DatabaseReference mQuestions;
    String userId="";
    String CorrectAnswer="";
    String WorkType="";
    String no;
    int number=0;
    int correct=0;
    Button Option1,Option2,Option3,Option4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_questions);
        Questions=(TextView)findViewById(R.id.Questions);
        Time=(TextView)findViewById(R.id.time);
        QuestionNumber=(TextView)findViewById(R.id.Questions_no);
        Option1=(Button)findViewById(R.id.Option1);
        Option2=(Button)findViewById(R.id.Option2);
        Option3=(Button)findViewById(R.id.Option3);
        Option4=(Button)findViewById(R.id.Option4);
        Intent intent=new Intent();
        type=intent.getStringExtra("Type");

        WorkType=intent.getStringExtra("JType");
        number=intent.getIntExtra("Number",0);

        correct=intent.getIntExtra("Correct",0);
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        Start_time();
        try {
            Log.i("ok","---------------------------Done---------------------"+userId+"----------");
            mQuestions = FirebaseDatabase.getInstance().getReference("QuestionAnswers/BasicQuestion");
            getQuestions();
        }
        catch (Exception e){
            Log.i("error","--------------------No history found--------------");
            Toast.makeText(this,"" +e,Toast.LENGTH_SHORT).show();

        }
        Option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CorrectAnswer.equals("O1")) {
                    correct++;
                }
                if(number<6){
                    Intent intent=new Intent(BasicQuestions.this,BasicQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(BasicQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
            }
        });
        Option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CorrectAnswer.equals("O2")) {
                    correct++;
                }
                if(number<6){
                    Intent intent=new Intent(BasicQuestions.this,BasicQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(BasicQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
            }
        });
        Option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CorrectAnswer.equals("O3")) {
                    correct++;
                }
                if(number<6){
                    Intent intent=new Intent(BasicQuestions.this,BasicQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(BasicQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
            }
        });
        Option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CorrectAnswer.equals("O4")) {
                    correct++;
                }
                if(number<6){
                    Intent intent=new Intent(BasicQuestions.this,BasicQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(BasicQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
            }
        });
    }

    public void Start_time(){
        CountDownTimer countDownTimer=new CountDownTimer(15000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer((int)millisUntilFinished/1000);

            }

            @Override
            public void onFinish() {
                Log.i("Finished","Timer all done");
                if(number<6){
                    Intent intent=new Intent(BasicQuestions.this,BasicQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(BasicQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Basic");
                    intent.putExtra("Number",number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",correct);


                    finish();
                    return;
                }

            }
        }.start();
    }
    public void updateTimer(int secondsLeft){
        String secondString=Integer.toString(secondsLeft);
        Time.setText(secondString);
    }
    public void getQuestions() {
        mQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void CheckAnswer(){

    }
}