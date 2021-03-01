package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Best Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int layout_code = getIntent().getIntExtra("layout_code", -1);

        if (layout_code == 0) {
            /////////// recycler view code
            recyclerView = findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            /////////// recycler view code
        }
        else if (layout_code == 1) {
            /////////// grid view code
            gridView = findViewById(R.id.grid_view);
            gridView.setVisibility(View.VISIBLE);

            List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.architect, "Architect"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.beauty_care, "Beauty Care"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.carpenter, "Carpenter"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.electrician, "Electrician"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.home_cleaning, "Home Cleaning"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.labour, "Labours"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mechanic, "Mechanic"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.painter, "Painter"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.plumber, "Plumber"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.technician, "Technician"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.architect, "Architect"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.beauty_care, "Beauty Care"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.carpenter, "Carpenter"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.electrician, "Electrician"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.home_cleaning, "Home Cleaning"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.labour, "Labours"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mechanic, "Mechanic"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.painter, "Painter"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.plumber, "Plumber"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.technician, "Technician"));

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridView.setAdapter(gridProductLayoutAdapter);
            /////////// grid view code
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}