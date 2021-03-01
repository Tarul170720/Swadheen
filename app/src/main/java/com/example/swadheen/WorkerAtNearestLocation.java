package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

public class WorkerAtNearestLocation extends AppCompatActivity {

    String Worker_Selected="";
    TextView worker;
    ListView getWorkers;
    LocationManager locationManager;
    LocationListener locationListener;
    Location nLastLocaton;
    private LatLng pickupLocation;
    ArrayList<String> Worker=new ArrayList<String>();
    ArrayList<String> WorkerId=new ArrayList<String>();
    private int radius = 1;
    private boolean WorkerFound = false;
    private String WorkerFoundId;
    GeoQuery geoQuery;
    private Boolean requestBol=false;
    private  String destination;
    RecyclerView rcv;
    List<NearbyworkerModel> testModelList = new ArrayList<>();

    Activity context;


    String data="";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                nLastLocaton=lastKnownLocation;

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_at_nearest_location);

        Intent intent=getIntent();
        Worker_Selected=intent.getStringExtra("CategoryName");

        worker=(TextView)findViewById(R.id.worker_selected);
        worker.setText(Worker_Selected);



    //    getWorkers=findViewById(R.id.workerList);
        rcv=(RecyclerView)findViewById(R.id.rcl);

        rcv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(WorkerAtNearestLocation.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv.setLayoutManager(layoutManager);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("Location",location.toString()) ;
                nLastLocaton=location;
            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            nLastLocaton=lastKnownLocation;
        }
        pickupLocation = new LatLng(nLastLocaton.getLatitude(), nLastLocaton.getLongitude());
        getClosestDriver();




    }
    String name;
    String phone;

    private void getClosestDriver() {
        if (radius>=100){
            worker.setText("Completed");
            return;
        }


        DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child("WorkerAvailable").child(Worker_Selected);

        GeoFire geoFire = new GeoFire(driversLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), 100);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {



                    WorkerFoundId = key;
                    name ="";
                    phone="";
                    Worker.add(data);
                    WorkerId.add(key);
                  getDriverLocation(key);

                    worker.setText("Looking For Worker....");



            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {



                    radius++;


            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    String image;
    private DatabaseReference driverRef;
    private ValueEventListener driverLocationListner;
    private void getDriverLocation(String workerFoundId) {
        driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(workerFoundId);
        driverLocationListner=driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                    if(map.get("Name")!=null)
                    {
                        name=map.get("Name").toString();

                    }
                    if(map.get("Mobile Number")!=null) {
                        phone = map.get("Mobile Number").toString();
                    }
                    if(name!=null&&phone!=null){
                        worker.setText(Worker_Selected);

                        DatabaseReference Imageref = FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(workerFoundId).child("image");

                        Imageref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    try{
                                    image=snapshot.getValue().toString();}
                                    catch (Exception e){
                                        Log.i("Error",e.toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        testModelList.add(new NearbyworkerModel(name, workerFoundId,Worker_Selected,image));
                        NearbyworkerAdaptere testAdapter = new NearbyworkerAdaptere(WorkerAtNearestLocation.this,testModelList);
                        rcv.setAdapter(testAdapter);
                        testAdapter.notifyDataSetChanged();



                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}