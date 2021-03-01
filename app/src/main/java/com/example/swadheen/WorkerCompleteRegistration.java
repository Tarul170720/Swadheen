package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.Reference;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.providers.PooledExecutorsProvider;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WorkerCompleteRegistration extends AppCompatActivity {

    EditText sQualification,sstream,syear,cQualification,cstream,cyear;
    Spinner job_type;
    TextView uprofile;
    Button  uploadFrontImageBtn, uploadBackImageBtn,sumbit;
    private DatabaseReference mWorkerDatabase;
    private FirebaseAuth mAuth;
    String user_id;
    private StorageReference mStorageRef;

    private String userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_complete_registration);


        job_type=(Spinner) findViewById(R.id.Job_type);

        sQualification=(EditText) findViewById(R.id.squalifactio);
        sstream=(EditText) findViewById(R.id.sstream);
        syear=(EditText) findViewById(R.id.syear);
        sumbit=(Button)findViewById(R.id.Sumbit);

        cQualification=(EditText) findViewById(R.id.cqualifactio);
        cstream=(EditText) findViewById(R.id.cstream);
        cyear=(EditText) findViewById(R.id.cyear);



        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent=getIntent();

        user_id=intent.getStringExtra("UserId");
        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(user_id);


        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });


    }
    private void mainIntent() {
        Intent mainIntent = new Intent(WorkerCompleteRegistration.this, WorkerSideSubCategory.class);
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
        userdata.put("Job_type",job_type.getSelectedItem().toString());


        mWorkerDatabase.updateChildren(userdata)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            mainIntent();
                            Toast.makeText(WorkerCompleteRegistration.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String error = task.getException().getMessage();
                            Toast.makeText(WorkerCompleteRegistration.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }







}