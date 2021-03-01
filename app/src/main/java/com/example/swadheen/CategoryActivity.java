package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CategoryActivity extends AppCompatActivity {
    Button basic,intermediate,high;
    String Job="";
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Toast.makeText(getApplicationContext(),"Home Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_profile:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(CategoryActivity.this,DriverProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_completed:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent1 = new Intent(CategoryActivity.this,TaskCompleted.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.menu_pending:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(CategoryActivity.this,QuestionAnswer.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.menu_out:
                        System.exit(0);
                        break;
                    case R.id.menu_call:
                        Toast.makeText(getApplicationContext(),"Call Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_settings:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;
                }
                return true;
            }
        });

        basic=(Button)findViewById(R.id.basic);
        intermediate=(Button)findViewById(R.id.intermediate);
        high=(Button)findViewById(R.id.High);
        Intent get=new Intent();
        Job=get.getStringExtra("Job_type");
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryActivity.this,BasicQuestions.class);
                startActivity(intent);
                intent.putExtra("Type","Basic");
                intent.putExtra("Number",1);
                intent.putExtra("JType",Job);
                intent.putExtra("Correct",0);


                finish();
                return;
            }
        });
        intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryActivity.this,BasicQuestions.class);
                startActivity(intent);
                intent.putExtra("Type","Intermediate");

                intent.putExtra("Number",1);
                intent.putExtra("JType",Job);
                intent.putExtra("Correct",0);

                finish();
                return;
            }
        });
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryActivity.this,WorkStatus.class);
                startActivity(intent);
                intent.putExtra("Type","High");
                finish();
                return;
            }
        });
    }
}