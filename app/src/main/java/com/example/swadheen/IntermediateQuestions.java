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

import java.util.Map;

public class IntermediateQuestions extends AppCompatActivity {
        String type="";
        TextView Questions,QuestionNumber,Time;
        FirebaseAuth mAuth;
        DatabaseReference mQuestions;
        String userId="";
        String CorrectAnswer="";
        String WorkType="";
        String no;
        int number;
        int correct=0;
        Button Option1,Option2,Option3,Option4;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Questions=(TextView)findViewById(R.id.Questions);
        Time=(TextView)findViewById(R.id.time);
        QuestionNumber=(TextView)findViewById(R.id.Questions_no);
        Option1=(Button)findViewById(R.id.Option1);
        Option2=(Button)findViewById(R.id.Option2);
        Option3=(Button)findViewById(R.id.Option3);
        Option4=(Button)findViewById(R.id.Option4);
        Intent intent=new Intent();
        type=intent.getStringExtra("Type");
        no=intent.getStringExtra("Number");
        WorkType=intent.getStringExtra("JType");
        number=Integer.parseInt(no);
        correct=Integer.parseInt(intent.getStringExtra("Correct"));
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        Start_time();
        try {
            Log.i("ok","---------------------------Done---------------------"+userId+"----------");
            mQuestions = FirebaseDatabase.getInstance().getReference().child("Qestions").child(WorkType).child("Basic").child(no);
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
                    Intent intent=new Intent(IntermediateQuestions.this,IntermediateQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(IntermediateQuestions.this,IntermediateQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


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
                    Intent intent=new Intent(IntermediateQuestions.this,IntermediateQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(IntermediateQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


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
                    Intent intent=new Intent(IntermediateQuestions.this,IntermediateQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(IntermediateQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


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
                    Intent intent=new Intent(IntermediateQuestions.this,IntermediateQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(IntermediateQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


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
                    Intent intent=new Intent(IntermediateQuestions.this,IntermediateQuestions.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


                    finish();
                    return;
                }
                else{
                    Intent intent=new Intent(IntermediateQuestions.this,results.class);
                    startActivity(intent);
                    number++;
                    intent.putExtra("Type","Intermediate");
                    intent.putExtra("Number",""+number);
                    intent.putExtra("JType",WorkType);
                    intent.putExtra("Correct",""+correct);


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
                Log.i("Yes","--------------------Got inside--------------");

                Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                String p="CorrectAnswer";
                String q="Question";
                String O1="Option 1";
                String O2="Option 2";
                String O3="Option 3";
                String O4="Option 4";
                if(map.get("Question")!=null)
                {
                    q=map.get("Question").toString();
                    Questions.setText(q);

                }
                if(map.get("O1")!=null) {
                    O1 = map.get("O1").toString();
                    Option1.setText(O1);
                }

                if(map.get("O1")!=null) {
                    O1 = map.get("O1").toString();
                    Option1.setText(O1);
                }

                if(map.get("O2")!=null) {
                    O2 = map.get("O2").toString();
                    Option2.setText(O2);
                }
                if(map.get("O3")!=null) {
                    O3 = map.get("O3").toString();
                    Option3.setText(O3);
                }

                if(map.get("O4")!=null) {
                    O4 = map.get("O4").toString();
                    Option4.setText(O4);
                }

                if(map.get("CorrectAnswer")!=null){
                    p=map.get("CorrectAnswer").toString();
                    CorrectAnswer=p;
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