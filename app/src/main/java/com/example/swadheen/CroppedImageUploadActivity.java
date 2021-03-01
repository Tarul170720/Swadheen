package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class CroppedImageUploadActivity extends AppCompatActivity {

    private Button croppedChooseImgBtn;
    private Button croppedUploadImgBtn;
    private Button next_page;
    private ImageView croppedImgView;
    String user_id;


    private EditText tv_aadhaar_no,upid;

    private static final int pick = 2;

    private Uri image;
    private Uri resultUri;

    private StorageReference str;
    private DatabaseReference dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropped_image_upload);


        Intent intent=getIntent();

        user_id=intent.getStringExtra("UserId");
        dB= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(user_id);
        str = FirebaseStorage.getInstance().getReference().child("Profile Image").child(user_id);

        dB.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                String image1 = dataSnapshot.getValue().toString();
                Picasso.get()
                        .load(image1)
                        .into(croppedImgView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        croppedChooseImgBtn = findViewById(R.id.cropChooseImgBtn);
        croppedUploadImgBtn = findViewById(R.id.cropUploadImgBtn);
        croppedImgView = findViewById(R.id.cropImageView);

        next_page=findViewById(R.id.Next_Page);
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntent();
            }
        });

        croppedChooseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, pick);
            }
        });

        croppedUploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadCroppedImage();
                CheckAadhaarNumber();
            }
        });

        tv_aadhaar_no = findViewById(R.id.tv_Aadhaar_Number);
        upid=findViewById(R.id.upid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pick && resultCode == RESULT_OK && data != null){
            image = data.getData();

            CropImage.activity(image)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void UploadCroppedImage(){
        str.child("Profile Image").putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> data = new HashMap<>();
                        String newUri = uri.toString();
                        data.put("image", newUri);
                        data.put("Aadhaar card",tv_aadhaar_no.getText().toString());
                        data.put("Upid",upid.getText().toString());
                        dB.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Image Stored", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void CheckAadhaarNumber(){
        String num = tv_aadhaar_no.getText().toString();
        boolean result = Verhoeff.validateVerhoeff(num);
        String msg = String.valueOf(result);

        if (msg == "true"){
            Toast.makeText(getApplicationContext(), "Valid Aadhaar Number", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Enter Valid Aadhaar Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextIntent() {
        Intent mainIntent = new Intent(CroppedImageUploadActivity.this,WorkerCompleteRegistration.class);
        mainIntent.putExtra("UserId", user_id);

        startActivity(mainIntent);
        finish();

    }
}