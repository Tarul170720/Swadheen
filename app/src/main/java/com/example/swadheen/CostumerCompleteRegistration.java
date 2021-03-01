package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CostumerCompleteRegistration extends AppCompatActivity {

    ImageView profilePhoto;
    EditText sQualification,sstream,syear,cQualification,cstream,cyear;
    Spinner job_type;

    Spinner spinner_medium;
    Spinner spinner_large;
    Spinner spinner;
    String  selectedSpinner;
    private  EditText etCity, etAddress, etStore, etInstitute;
    private EditText etOther_medium;
    private EditText etLarge_Website;
    Button sumbit;
    private DatabaseReference mWorkerDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    String user_id;

    private String userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_complete_registration);


        sQualification=(EditText) findViewById(R.id.squalifactio);
        sstream=(EditText) findViewById(R.id.sstream);
        syear=(EditText) findViewById(R.id.syear);
        sumbit=(Button)findViewById(R.id.Sumbit);


        cQualification=(EditText) findViewById(R.id.cqualifactio);
        cstream=(EditText) findViewById(R.id.cstream);
        cyear=(EditText) findViewById(R.id.cyear);


        etCity = (EditText) findViewById(R.id.etCity);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etStore = (EditText) findViewById(R.id.etStore);
        etInstitute = (EditText) findViewById(R.id.etInstitute);

        spinner = findViewById(R.id.aSpinner);
        spinner_medium = findViewById(R.id.aSpinner_medium);
        spinner_large=findViewById(R.id.aSpinner_Large);

        etOther_medium = (EditText) findViewById(R.id.etOther_medium);
        etLarge_Website = (EditText) findViewById(R.id.etLarge_Website);


        profilePhoto=(ImageView)findViewById(R.id.imageView);

        Intent intent=getIntent();
        user_id=intent.getStringExtra("UserId");

        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Costumer").child(user_id);


        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();

                selectSpinner();

                Intent intent=new Intent(CostumerCompleteRegistration.this,UserMainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedSpinner = parent.getItemAtPosition(position).toString();
                switch (selectedSpinner)
                {
                    case "None":
                        // assigning div item list defined in XML to the div Spinner
                        etCity.setVisibility(GONE);
                        etAddress.setVisibility(GONE);
                        etStore.setVisibility(GONE);
                        etInstitute.setVisibility(GONE);
                        etLarge_Website.setVisibility(GONE);
                        spinner_medium.setVisibility(GONE);
                        break;

                    case "Small":
                        etCity.setVisibility(VISIBLE);
                        etAddress.setVisibility(VISIBLE);
                        etStore.setVisibility(GONE);
                        etInstitute.setVisibility(GONE);
                        etLarge_Website.setVisibility(GONE);
                        spinner_medium.setVisibility(GONE);
                        break;

                    case "Medium":
                        etCity.setVisibility(VISIBLE);
                        etAddress.setVisibility(VISIBLE);
                        etStore.setVisibility(VISIBLE);
                        etInstitute.setVisibility(GONE);
                        etLarge_Website.setVisibility(GONE);
                        spinner_medium.setVisibility(View.VISIBLE);
                        spinner_large.setVisibility(GONE);
                        break;

                    case "Large":
                        etCity.setVisibility(VISIBLE);
                        etAddress.setVisibility(VISIBLE);
                        etInstitute.setVisibility(VISIBLE);
                        etLarge_Website.setVisibility(VISIBLE);
                        etStore.setVisibility(GONE);
                        spinner_large.setVisibility(View.VISIBLE);
                        spinner_medium.setVisibility(GONE);
                        break;
                }

                //set spinner_medium Visibility to Visible
                // spinner_medium.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

    }
    private void mainIntent() {
        Intent mainIntent = new Intent(CostumerCompleteRegistration.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void saveInfo(){
        Map userdata = new HashMap<>();
        userdata.put("sQualification",sQualification.getText().toString());
        userdata.put("sstream",sstream.getText().toString());
        userdata.put("syear", syear.getText().toString());
        userdata.put("cQualification", cQualification.getText().toString());
        userdata.put("cstream",cstream.getText().toString());
        userdata.put("cyear",cyear.getText().toString());

        mWorkerDatabase.updateChildren(userdata)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CostumerCompleteRegistration.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String error = task.getException().getMessage();
                            Toast.makeText(CostumerCompleteRegistration.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void selectSpinner() {

        DatabaseReference job=mWorkerDatabase.child("Job");
        if (spinner.getSelectedItem().toString().trim().equals("Small")) {
            if (!TextUtils.isEmpty(etCity.getText()) && !TextUtils.isEmpty(etAddress.getText())) {

                Map data = new HashMap<>();
                data.put("Entrepreneur", spinner.getSelectedItem().toString().trim());
                data.put("City", etCity.getText().toString());
                data.put("Address", etAddress.getText().toString());


                job.updateChildren(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CostumerCompleteRegistration.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CostumerCompleteRegistration.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{

            }

        }

        else if (spinner.getSelectedItem().toString().trim().equals("Medium")){
            if(!TextUtils.isEmpty(etCity.getText()) && !TextUtils.isEmpty(etAddress.getText()) && !TextUtils.isEmpty(etStore.getText())){
                Map data = new HashMap<>();
                data.put("Entrepreneur", spinner.getSelectedItem().toString().trim());
                data.put("City", etCity.getText().toString());
                data.put("Address", etAddress.getText().toString());
                data.put("StoreName", etStore.getText().toString());
                data.put("StoreType",spinner_medium.getSelectedItem().toString().trim());


                job.updateChildren(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CostumerCompleteRegistration.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CostumerCompleteRegistration.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }

        else if (spinner.getSelectedItem().toString().trim().equals("Large")){
            if(!TextUtils.isEmpty(etCity.getText()) && !TextUtils.isEmpty(etAddress.getText())
                    && !TextUtils.isEmpty(etInstitute.getText()) && !TextUtils.isEmpty(etLarge_Website.getText())){
                Map userdata = new HashMap<>();
                userdata.put("Entrepreneur", spinner.getSelectedItem().toString().trim());
                userdata.put("City", etCity.getText().toString());
                userdata.put("Address", etAddress.getText().toString());
                userdata.put("InstituteName", etInstitute.getText().toString());
                userdata.put("WebsiteUrl", etLarge_Website.getText().toString());
                userdata.put("InstituteType", spinner_large.getSelectedItem().toString().trim());



                job.updateChildren(userdata)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(CostumerCompleteRegistration.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CostumerCompleteRegistration.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }
    }


}