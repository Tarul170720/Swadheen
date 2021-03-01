package com.example.swadheen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swadheen.R;
import com.example.swadheen.SelectSignInFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInUserFragment extends Fragment {

    public SignInUserFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccountUser;
    private TextView goBackUser;
    private FrameLayout parentFrameLayout;

    private EditText emailUser;
    private EditText passwordUser;

    private ImageButton closeBtnUser;
    private Button signInBtnUser;

    private String emailPatternUser = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private ProgressBar progressBarUser;

    private TextView forgotPasswordUser;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_user, container, false);
        dontHaveAnAccountUser = view.findViewById(R.id.tv_user_dont_have_an_account);
        goBackUser = view.findViewById(R.id.tv_user_goback);

        emailUser = view.findViewById(R.id.sign_in_user_email);
        passwordUser = view.findViewById(R.id.sign_in_user_password);

        closeBtnUser = view.findViewById(R.id.sign_in_user_close_btn);
        signInBtnUser = view.findViewById(R.id.sign_in_user_btn);
        mAuth= FirebaseAuth.getInstance();
        firebaseAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(getActivity(),Costumer.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
            }
        };

        progressBarUser = view.findViewById(R.id.sign_in_user_progressbar);

        forgotPasswordUser = view.findViewById(R.id.sign_in_user_forgot_password);

        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccountUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });

        goBackUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SelectSignInFragment());
            }
        });
        signInBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=emailUser.getText().toString();
                final String password=passwordUser.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getActivity(),"sing in error",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent=new Intent(getActivity(),UserMainActivity.class);
                            startActivity(intent);

                            getActivity().finish();
                            return;
                        }

                    }
                });
            }
        });
        closeBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserMainActivity.class);
                startActivity(intent);

                getActivity().finish();
                return;
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_out_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


}