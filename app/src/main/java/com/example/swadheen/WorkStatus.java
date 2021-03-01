package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class WorkStatus extends AppCompatActivity {
    LinearLayout l1,l2,l3;
    ImageView iv1,iv2,iv3;
    String completed="No";
    private DatabaseReference mWorkerDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    String user_id;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_status);
        Intent intent=getIntent();
        completed=intent.getStringExtra("Done");
        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        l3=(LinearLayout)findViewById(R.id.l3);
        iv1=(ImageView)findViewById(R.id.iv1);
        iv2=(ImageView)findViewById(R.id.iv2);
        iv3=(ImageView)findViewById(R.id.iv3);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        user_id=mAuth.getCurrentUser().getUid();
        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(user_id);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WorkStatus.this,ClassifierActivity.class);
                intent.putExtra("Type","0 Before");
                startActivity(intent);

                finish();
                return;
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WorkStatus.this,ClassifierActivity.class);
                intent.putExtra("Type","2 During Work");
                startActivity(intent);

                finish();
                return;

            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WorkStatus.this,ClassifierActivity.class);
                intent.putExtra("Type","1 After Work");
                startActivity(intent);

                finish();
                return;

            }
        });

        int i=0,j=0,k=0;
        if(completed!=null){


        if(completed.equals("OK1")){
            iv1.setImageResource(R.drawable.ic_baseline_check_24);
            i++;
        }
        if(completed.equals("OK2")){
            iv2.setImageResource(R.drawable.ic_baseline_check_24);
            j++;
        }
        if(completed.equals("OK3")){
            iv3.setImageResource(R.drawable.ic_baseline_check_24);
            k++;
        }}
        if(i==1&&j==1&&k==1){
            Map userdata = new HashMap<>();
            userdata.put("Verified","Ok");
            mWorkerDatabase.updateChildren(userdata)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                            }
                            else {

                                String error = task.getException().getMessage();
                                Toast.makeText(WorkStatus.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}