package com.example.swadheen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private DocumentReference ref;

    private EditText etName, etEmail, etMobileNumber, etPassword, etConfirmPassword;
    private  EditText etCity, etAddress, etStore, etInstitute;

    private Button btnSignUp;
    private ProgressBar progressBarUser;





    String userId="";
    private DatabaseReference mWorkerDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private String emailPatternUser = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);


        btnSignUp = (Button) findViewById(R.id.sign_up_user_btn);
        progressBarUser = findViewById(R.id.sign_up_user_progressbar);


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(SignUpActivity.this,UserMainActivity.class);
                    startActivity(intent);
                    return;
                }
            }
        };
        //   ref = firebaseFirestore.document("");



        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });


    }


    private void checkInputs() {
        if (!TextUtils.isEmpty(etMobileNumber.getText())) {
            if (!TextUtils.isEmpty(etName.getText())) {
                if (!TextUtils.isEmpty(etEmail.getText())) {
                    if (!TextUtils.isEmpty(etPassword.getText()) && etPassword.length() >= 8) {
                        if (!TextUtils.isEmpty(etConfirmPassword.getText())) {
                            btnSignUp.setEnabled(true);
                        } else {
                            btnSignUp.setEnabled(false);
                            btnSignUp.setTextColor(Color.argb(50, 0, 0, 0));
                        }

                    } else {
                        btnSignUp.setEnabled(false);
                        btnSignUp.setTextColor(Color.argb(50, 0, 0, 0));
                    }
                }
            } else {
                btnSignUp.setEnabled(false);
                btnSignUp.setTextColor(Color.argb(50, 0, 0, 0));
            }
        } else {
            btnSignUp.setEnabled(false);
            btnSignUp.setTextColor(Color.argb(50, 0, 0, 0));
        }
    }
    private void checkEmailAndPassword() {
        if (etEmail.getText().toString().matches(emailPatternUser)) {
            if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                progressBarUser.setVisibility(View.VISIBLE);
                btnSignUp.setEnabled(false);
                btnSignUp.setTextColor(Color.argb(50,0,0,0));

                firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String user_id=firebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("User").child("Costumers").child(user_id);
                                    current_user_db.setValue(true);
                                    userId=user_id;

                                    mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Costumers").child(user_id);
                                    saveUserInfo();



                                }
                                else {
                                    progressBarUser.setVisibility(View.INVISIBLE);
                                    btnSignUp.setEnabled(true);
                                    btnSignUp.setTextColor(Color.rgb(0,0,0));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
            else {
                etConfirmPassword.setError("Password doesn't matched!");
            }
        }
        else {
            etEmail.setError("Invalid Email!");
        }
    }


    private void saveUserInfo(){
        Map userdata = new HashMap<>();
        userdata.put("Name", etName.getText().toString());
        userdata.put("Email", etEmail.getText().toString());
        userdata.put("Mobile Number", etMobileNumber.getText().toString());
        userdata.put("Password", etPassword.getText().toString());
        userdata.put("Coniform Password", etConfirmPassword.getText().toString());

        mWorkerDatabase.updateChildren(userdata)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignUpActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressBarUser.setVisibility(View.INVISIBLE);
                            btnSignUp.setEnabled(true);
                            btnSignUp.setTextColor(Color.rgb(0,0,0));
                            String error = task.getException().getMessage();
                            Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Intent intent=new Intent(SignUpActivity.this,CroppedImageUserActivity.class);
        intent.putExtra("UserId",userId);
        startActivity(intent);
        finish();
        return;


    }
}


