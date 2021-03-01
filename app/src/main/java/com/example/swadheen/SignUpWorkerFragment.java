package com.example.swadheen;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarException;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class SignUpWorkerFragment extends Fragment {

    public SignUpWorkerFragment() {
        // Required empty public constructor
    }

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQEUST_CODE = 10001;


    private TextView alreadyHaveAnAccountWorker;
    private FrameLayout parentFrameLayout;

    private EditText phoneNoWorker;
    private EditText emailWorker;
    private EditText fullNameWorker;
    private EditText passwordWorker;
    private EditText confirmPasswordWorker;

    private FirebaseAuth mAuth;
    private DatabaseReference mWorkerDatabase;
    private ImageButton closeBtnWorker;
    private Button signUpBtnWorker;
    private ProgressBar progressBarWorker;
    private ImageView image;
    private Button takePicture;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;

    private String userId = "";
    private Uri resultUri;

    private String emailPatternWorker = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    final int IMAGE_REQUEST = 7;
    final int IMAGE_REQUEST_1 = 8;
    private Uri imageLocationPath;
    String selectedSpinner, selectedSpinnerType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_worker, container, false);
        alreadyHaveAnAccountWorker = view.findViewById(R.id.tv_worker_already_have_an_account);

        phoneNoWorker = view.findViewById(R.id.sign_up_worker_phoneText);
        emailWorker = view.findViewById(R.id.sign_up_worker_email);
        fullNameWorker = view.findViewById(R.id.sign_up_worker_fullname);
        passwordWorker = view.findViewById(R.id.sign_up_worker_password);
        confirmPasswordWorker = view.findViewById(R.id.sign_up_worker_confirm_password);

        closeBtnWorker = view.findViewById(R.id.sign_up_worker_close_btn);
        signUpBtnWorker = view.findViewById(R.id.sign_up_worker_btn);
        image = view.findViewById(R.id.sign_up_worker_logo);
        takePicture = view.findViewById(R.id.upload);

        // initalizing ui elements
        initializeUIElements();


        progressBarWorker = view.findViewById(R.id.sign_up_worker_progressbar);

        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        firebaseAuth = FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        alreadyHaveAnAccountWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpWorkerFragment());
            }
        });

        closeBtnWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        emailWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        fullNameWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPasswordWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signUpBtnWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();



            }
        });

    }

    private void initializeUIElements() {


        // adding on click listener to button
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking whether camera permissions are available.
                // if permission is avaialble then we open camera intent to get picture
                // otherwise reqeusts for permissions
                if (hasPermission()) {
                    openCamera();
                } else {
                    requestPermission();
                }
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_from_left, R.anim.slid_out_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(phoneNoWorker.getText())) {
            if (!TextUtils.isEmpty(fullNameWorker.getText())) {
                if (!TextUtils.isEmpty(emailWorker.getText())) {
                    if (!TextUtils.isEmpty(passwordWorker.getText()) && passwordWorker.length() >= 8) {
                        if (!TextUtils.isEmpty(confirmPasswordWorker.getText())) {
                            signUpBtnWorker.setEnabled(true);
                            signUpBtnWorker.setTextColor(Color.rgb(0, 0, 0));
                        } else {
                            signUpBtnWorker.setEnabled(false);
                            signUpBtnWorker.setTextColor(Color.argb(50, 0, 0, 0));
                        }
                    } else {
                        signUpBtnWorker.setEnabled(false);
                        signUpBtnWorker.setTextColor(Color.argb(50, 0, 0, 0));
                    }
                } else {
                    signUpBtnWorker.setEnabled(false);
                    signUpBtnWorker.setTextColor(Color.argb(50, 0, 0, 0));
                }
            } else {
                signUpBtnWorker.setEnabled(false);
                signUpBtnWorker.setTextColor(Color.argb(50, 0, 0, 0));
            }
        } else {
            signUpBtnWorker.setEnabled(false);
            signUpBtnWorker.setTextColor(Color.argb(50, 0, 0, 0));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // if this is the result of our camera image request
        if (requestCode == CAMERA_REQEUST_CODE) {
            if (data != null) {
                // getting bitmap of the image
                Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
                // displaying this bitmap in imageview
                image.setImageBitmap(photo);


            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if this is the result of our camera permission request
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (hasAllPermissions(grantResults)) {
                openCamera();
            } else {
                requestPermission();
            }
        }
    }

    /**
     * checks whether all the needed permissions have been granted or not
     *
     * @param grantResults the permission grant results
     * @return true if all the reqested permission has been granted,
     * otherwise returns false
     */
    private boolean hasAllPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    /**
     * Method requests for permission if the android version is marshmallow or above
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // whether permission can be requested or on not
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(getActivity(), "Camera Permission Required", Toast.LENGTH_SHORT).show();
            }
            // request the camera permission permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * creates and starts an intent to get a picture from camera
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQEUST_CODE);
    }

    /**
     * checks whether camera permission is available or not
     *
     * @return true if android version is less than marshmallo,
     * otherwise returns whether camera permission has been granted or not
     */

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    private void checkEmailAndPassword() {
        if (emailWorker.getText().toString().matches(emailPatternWorker)) {
            if (passwordWorker.getText().toString().equals(confirmPasswordWorker.getText().toString())) {

                progressBarWorker.setVisibility(View.VISIBLE);
                signUpBtnWorker.setEnabled(false);
                signUpBtnWorker.setTextColor(Color.argb(50, 0, 0, 0));


                firebaseAuth.createUserWithEmailAndPassword(emailWorker.getText().toString(), passwordWorker.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String user_id = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("User").child("Workers").child(user_id);
                                    current_user_db.setValue(true);
                                    userId = user_id;


                                    mWorkerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(user_id);
                                    saveUserInfo();
                                    Intent mainIntent = new Intent(getActivity(), CroppedImageUploadActivity.class);
                                    mainIntent.putExtra("UserId", user_id);

                                    startActivity(mainIntent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "sing up error", Toast.LENGTH_SHORT).show();
                                    progressBarWorker.setVisibility(View.INVISIBLE);
                                    signUpBtnWorker.setEnabled(true);
                                    signUpBtnWorker.setTextColor(Color.rgb(0, 0, 0));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                confirmPasswordWorker.setError("Password doesn't matched!");
            }
        } else {
            emailWorker.setError("Invalid Email!");
        }
    }


    private void saveUserInfo() {
        Map userdata = new HashMap<>();
        userdata.put("Name", fullNameWorker.getText().toString());
        userdata.put("Email", emailWorker.getText().toString());
        userdata.put("Mobile Number", phoneNoWorker.getText().toString());
        userdata.put("Password", passwordWorker.getText().toString());

        mWorkerDatabase.updateChildren(userdata)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                        } else {
                            progressBarWorker.setVisibility(View.INVISIBLE);
                            signUpBtnWorker.setEnabled(true);
                            signUpBtnWorker.setTextColor(Color.rgb(0, 0, 0));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Code for Upload Image Button


}