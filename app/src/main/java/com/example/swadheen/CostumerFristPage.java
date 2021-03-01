package com.example.swadheen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CostumerFristPage extends AppCompatActivity {

    Button Painter,Cobbler,Carpainter,Mason,Plumber,sweeper,Tailor,Labour_for_Construction,Full_day_Labours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_frist_page);

        Painter=(Button)findViewById(R.id.Painter);
        Cobbler=(Button)findViewById(R.id.Cobbler);
        Carpainter=(Button)findViewById(R.id.Carpainter);
        Mason=(Button)findViewById(R.id.Mason);
        Plumber=(Button)findViewById(R.id.Plumber);
        sweeper=(Button)findViewById(R.id.Sweeper);
        Tailor=(Button)findViewById(R.id.Tailor);
        Labour_for_Construction=(Button)findViewById(R.id.Labour_For_Construction);
        Full_day_Labours=(Button)findViewById(R.id.Full_day_Labour);

        Painter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Painter");
                startActivity(intent);
                finish();
                return;

            }
        });

        Plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Plumber");
                startActivity(intent);
                finish();
                return;
            }
        });
        Cobbler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Cobbler");
                startActivity(intent);
                finish();
                return;

            }
        });
        Mason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Mason");
                startActivity(intent);
                finish();
                return;

            }
        });
        Carpainter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Carpainter");
                startActivity(intent);
                finish();
                return;

            }
        });
        sweeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Sweeper");
                startActivity(intent);
                finish();
                return;

            }
        });
        Tailor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Tailor");
                startActivity(intent);
                finish();
                return;

            }
        });
        Labour_for_Construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Labour_for_Construction");
                startActivity(intent);
                finish();
                return;

            }
        });
        Full_day_Labours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostumerFristPage.this,WorkerAtNearestLocation.class);
                intent.putExtra("Worker_Selected","Full_day_Labours");
                startActivity(intent);
                finish();
                return;

            }
        });


    }
}