package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyWorkerProfile extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    ImageView profilePhoto;
    TextView name,adhaar,jobs,sQualification,sstream,syear,cQualification,cstream,cyear,email,phone;
    RatingBar ratingBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mWorkerDatabase,mWorkerDatabase1;
    private String mProfieUrl;
    private String userId,mName,mPhone_Number,mEmail,mjobs,msQualification,msstream,msyear,mcQualification,mcstream,mcyear;
    private String Worker_Selected;
    private Button book;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationReques;
    String Woker_Type;
    private LatLng pickupLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_worker_profile);

        profilePhoto=(ImageView)findViewById(R.id.image);
        name=(TextView) findViewById(R.id.name);
        jobs=(TextView)findViewById(R.id.jobs);
        adhaar=(TextView)findViewById(R.id.adhaar);


        sQualification=(TextView)findViewById(R.id.squalifactio);
        sstream=(TextView)findViewById(R.id.sstream);
        syear=(TextView)findViewById(R.id.syear);
        book=(Button)findViewById(R.id.order);


        cQualification=(TextView)findViewById(R.id.cqualifactio);
        cstream=(TextView)findViewById(R.id.cstream);
        cyear=(TextView)findViewById(R.id.cyear);

        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);

        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        Intent intent=getIntent();
        Worker_Selected=intent.getStringExtra("Worker_key");
        Woker_Type=intent.getStringExtra("Woker_Type");
        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(Worker_Selected);

        mWorkerDatabase1= FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(Worker_Selected);

        buildGoogleApiClient();

        DatabaseReference driverAsking2= FirebaseDatabase.getInstance().getReference().child("Asked").child(Worker_Selected).child("Accepted");
        driverAsking2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getValue()!=null){
                        if(snapshot.getValue().toString().equals("Yes")){
                            DatabaseReference complete=FirebaseDatabase.getInstance().getReference("Completed").child(userId);
                            complete.setValue("No");

                            Intent intent1=new Intent(NearbyWorkerProfile.this,CostumerMapsActivity.class);
                            intent1.putExtra("worker_id",Worker_Selected);
                            intent1.putExtra("worker_type",Woker_Type);



                            startActivity(intent1);
                            finish();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        getMoreUserInfo();
        getUserInfo();
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setText("Wating For Worker");
                book.setEnabled(false);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("request");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }

                    }


                });
                HashMap map6 = new HashMap();
                map6.put(Worker_Selected,Woker_Type);

                ref.child(userId).child("Worker_Selected").updateChildren(map6);

                pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(Worker_Selected).child("costumerRequest");
                String costumerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap map = new HashMap();
                map.put("costumerRideId", costumerId);
                map.put("Job_Type",Woker_Type);
                driverRef.updateChildren(map);


                DatabaseReference refAailable= FirebaseDatabase.getInstance().getReference("WorkerAvailable").child(Woker_Type);

                DatabaseReference  workerRef = refAailable.child(Worker_Selected).child("l");
                workerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Object> map = (List<Object>) snapshot.getValue();
                            double locationLat = 0;
                            double locationLng = 0;

                            if (map.get(0) != null) {
                                locationLat = Double.parseDouble(map.get(0).toString());
                            }
                            if (map.get(1) != null) {
                                locationLng = Double.parseDouble(map.get(1).toString());
                            }

                            DatabaseReference refWorking=FirebaseDatabase.getInstance().getReference("WorkerWorking").child(Woker_Type);
                            GeoFire geoFireWorking=new GeoFire(refWorking);
                            GeoFire geoFireAvilable=new GeoFire(refAailable);
                            geoFireWorking.setLocation(Worker_Selected, new GeoLocation(locationLat,locationLng), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (error != null) {
                                        System.err.println("There was an error saving the location to GeoFire: " + error);
                                    } else {
                                        System.out.println("Location saved on server successfully!");
                                    }

                                }


                            });

                            geoFireAvilable.removeLocation(userId, new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (error != null) {
                                        System.err.println("There was an error saving the location to GeoFire: " + error);
                                    } else {
                                        System.out.println("Location saved on server successfully!");
                                    }
                                }
                            });


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                DatabaseReference driverAsking= FirebaseDatabase.getInstance().getReference().child("Asked").child(Worker_Selected);
                HashMap map5 = new HashMap();
                map5.put("Accepted", "Wating");


                driverAsking.updateChildren(map5);



            }
        });

    }
    private void getUserInfo(){
        mWorkerDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                    if(map.get("Name")!=null)
                    {
                        mName=map.get("Name").toString();
                        name.setText(mName);
                    }
                    if(map.get("Mobile Number")!=null) {
                        mPhone_Number = map.get("Mobile Number").toString();
                        phone.setText(mPhone_Number);
                    }
                    if(map.get("Email")!=null) {
                        mEmail = map.get("Email").toString();
                        email.setText(mEmail);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getMoreUserInfo(){
        mWorkerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();

                    if(map.get("sQualification")!=null)
                    {
                        msQualification=map.get("sQualification").toString();
                        sQualification.setText(msQualification);
                    }
                    if(map.get("sstrea")!=null) {
                        msstream= map.get("sstrea").toString();
                        sstream.setText(msstream);
                    }
                    if(map.get("syear")!=null) {
                        msyear = map.get("syear").toString();
                        syear.setText(msyear);
                    }
                    if(map.get("cQualification")!=null) {
                        mcQualification = map.get("cQualification").toString();
                        cQualification.setText(mcQualification);
                    }
                    if(map.get("cstream")!=null) {
                        mcstream = map.get("cstream").toString();
                        cstream.setText(mcstream);
                    }
                    if(map.get("cyear")!=null) {
                        mcyear = map.get("cyear").toString();
                        cyear.setText(mcyear);
                    }
                    if(map.get("Job_type")!=null) {
                        mjobs = map.get("Job_type").toString();
                        jobs.setText(mjobs);
                    }
                    if(map.get("image")!=null) {
                        mProfieUrl = map.get("image").toString();

                        Glide.with(getApplicationContext()).load(mProfieUrl).into(profilePhoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationReques = new LocationRequest();
        mLocationReques.setInterval(1000);
        mLocationReques.setFastestInterval(1000);
        mLocationReques.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationReques, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}