package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.Edits;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CostumerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    LocationRequest mLocationReques;
    private Boolean requestBol=false;
    private Button mLogout, mRequest,mSettings,confirm_buttom,confirm;
    private RatingBar rating;
    private String driveFoundId;
    private int cancelled=0;
    private float totalrate=0,num=0;
    private Location pickupLocation;
    private Marker pickupMarker;
    private CardView card1,card2;
    private float rate=0;
    String rated="";

    private  String destination;
    private String worker_type;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                pickupLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_maps);
        Intent intent=getIntent();
        driveFoundId=intent.getStringExtra("worker_id");
        worker_type=intent.getStringExtra("worker_type");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mLogout = (Button) findViewById(R.id.logout);
        mRequest = (Button) findViewById(R.id.request);
        confirm_buttom=(Button)findViewById(R.id.confirm_button);
        card1=(CardView)findViewById(R.id.cardV);
        card2=(CardView)findViewById(R.id.card2);
        rating=(RatingBar) findViewById(R.id.rating);
        confirm=(Button)findViewById(R.id.confirm);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(CostumerMapsActivity.this, "Thanks for rating :-}", Toast.LENGTH_SHORT).show();
                rate=rating;


            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelled = 1;
                if(driveFoundId!=null){


                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(driveFoundId).child("costumerRequest");
                    DatabaseReference driverRef1=FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(driveFoundId).child("costumerRequest");


                    driverRef.setValue(true);
                    driverRef1.setValue(true);


                }



                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("request");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.removeLocation(userId, new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }

                    }


                });
                if(pickupMarker!=null){
                    pickupMarker.remove();
                }

                if(mDriverMarker!=null){
                    mDriverMarker.remove();
                }

                driverFound=false;
                radius=1;
                userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference rating=FirebaseDatabase.getInstance().getReference("Rating").child("Worker").child(driveFoundId);
                Map data2 = new HashMap<>();
                data2.put(userId,rate);
                rating.updateChildren(data2)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CostumerMapsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CostumerMapsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                rating.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Iterator<DataSnapshot> items=snapshot.getChildren().iterator();
                            while(items.hasNext()){
                                DataSnapshot item=items.next();
                                if(item!=null){
                                    try{
                                        int rate=(Integer)item.getValue();

                                        totalrate=totalrate+rate;
                                    num++;}
                                    catch (Exception execption){
                                        Log.i("Error",execption.toString());
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                DatabaseReference RatingRef=FirebaseDatabase.getInstance().getReference("Users_Info").child("Workers").child(driveFoundId);
                Map data = new HashMap<>();
                if(num>0) {
                    totalrate = totalrate / num;
                }
                else {
                    totalrate=rate;
                }
                data.put("Rating",totalrate);
                RatingRef.updateChildren(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(CostumerMapsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(CostumerMapsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                DatabaseReference historyWorker=FirebaseDatabase.getInstance().getReference("Histroy").child("Worker").child(driveFoundId).child(formattedDate);

                Map data1= new HashMap<>();
                data1.put("Work type",worker_type);
                data1.put("Worker Id",driveFoundId);
                historyWorker.updateChildren(data1)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CostumerMapsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CostumerMapsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                Intent intent = new Intent(CostumerMapsActivity.this,Payment.class);
                intent.putExtra("WorkerId",driveFoundId);
                startActivity(intent);
                finish();





            }
        });



        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CostumerMapsActivity.this,NearbyWorkerProfile.class);
                startActivity(intent);
                finish();
                return;
            }
        });
            DatabaseReference complete = FirebaseDatabase.getInstance().getReference("Completed").child(driveFoundId);
            complete.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String completed = snapshot.getValue().toString();
                        if (completed.equals("Yes")) {
                            card1.setVisibility(View.GONE);
                            card2.setVisibility(View.VISIBLE);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        confirm_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference complete=FirebaseDatabase.getInstance().getReference("Completed").child(userId);
                complete.setValue("Yes");
                confirm_buttom.setText("Wating For Worker To Complete");

                card1.setVisibility(View.GONE);
                card2.setVisibility(View.VISIBLE);


                confirm_buttom.setEnabled(false);


                DatabaseReference costumerRef=FirebaseDatabase.getInstance().getReference("Histroy").child("Costumer").child(userId).child(formattedDate);

                Map data = new HashMap<>();
                data.put("Work type",worker_type);
                data.put("Worker Id",driveFoundId);

                costumerRef.updateChildren(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CostumerMapsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CostumerMapsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                DatabaseReference refAailable= FirebaseDatabase.getInstance().getReference("WorkerAvailable").child(worker_type);
                DatabaseReference refWorking=FirebaseDatabase.getInstance().getReference("WorkerWorking").child(worker_type);

                GeoFire geoFireWorking=new GeoFire(refWorking);
                geoFireWorking.removeLocation(userId, new GeoFire.CompletionListener() {
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
        });


        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card1.setVisibility(View.VISIBLE);


            }

        });



    }

    private int radius = 1;
    private boolean driverFound = false;

    GeoQuery geoQuery;
    private Marker mDriverMarker;
    private DatabaseReference driverRef;
    private ValueEventListener driverLocationListner;
    private void getDriverLocation() {
        driverRef = FirebaseDatabase.getInstance().getReference("WorkerWorking").child(worker_type).child(driveFoundId).child("l");
        driverLocationListner=driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Object> map = (List<Object>) snapshot.getValue();

                    double locationLat = 0;
                    double locationLng = 0;
                    mRequest.setText("Driver Found");
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng = new LatLng(locationLat, locationLng);
                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }
                    LatLng pickupLatLang = new LatLng(pickupLocation.getLatitude(), pickupLocation.getLongitude());


                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.getLatitude());
                    loc1.setLongitude(pickupLocation.getLongitude());
                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);


                    float distance = loc1.distanceTo(loc2);
                    if (distance < 100) {
                        mRequest.setText("Driver Available");
                    } else {
                        mRequest.setText("Driver Found" + String.valueOf(distance));
                    }
                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your Worker"));
                    String url = getUrl(driverLatLng,pickupLatLang);

                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void centerMapOnLocation(Location location,String title){
        if(location!=null) {

            if(getApplicationContext()!=null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title(title));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                if(cancelled==0)
                    getDriverLocation();
            }}}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("Location",location.toString()) ;
                centerMapOnLocation(location,"Your Location");
                pickupLocation=location;
            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            centerMapOnLocation(lastKnownLocation,"Your Location");
            pickupLocation=lastKnownLocation;
        }
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
    class DataParser {

        List<List<HashMap<String,String>>> parse(JSONObject jObject){

            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude) );
                                hm.put("lng", Double.toString((list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }


            return routes;
        }


        /**
         * Method to decode polyline points
         * */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }




}