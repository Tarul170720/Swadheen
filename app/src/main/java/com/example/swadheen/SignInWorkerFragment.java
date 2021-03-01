package com.example.swadheen;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInWorkerFragment extends Fragment {

    public SignInWorkerFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccountWorker;
    private TextView goBackWorker;
    private FrameLayout parentFrameLayout;

    private EditText emailWorker;
    private EditText passwordWorker;

    private ImageButton closeBtnWorker;
    private Button signInBtnWorker;

    private String emailPatternWorker = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private ProgressBar progressBarWorker;

    private TextView forgotPasswordWorker;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_worker, container, false);
        dontHaveAnAccountWorker = view.findViewById(R.id.tv_worker_dont_have_an_account);
        goBackWorker = view.findViewById(R.id.tv_worker_goback);

        emailWorker = view.findViewById(R.id.sign_in_worker_email);
        passwordWorker = view.findViewById(R.id.sign_in_worker_password);

        mAuth= FirebaseAuth.getInstance();
        firebaseAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
            }
        };

        closeBtnWorker = view.findViewById(R.id.sign_in_worker_close_btn);
        signInBtnWorker = view.findViewById(R.id.sign_in_worker_btn);

        progressBarWorker = view.findViewById(R.id.sign_in_worker_progressbar);

        forgotPasswordWorker = view.findViewById(R.id.sign_in_worker_forgot_password);

        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccountWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpWorkerFragment());
            }
        });

        goBackWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SelectSignInFragment());
            }
        });

        signInBtnWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=emailWorker.getText().toString();
                final String password=passwordWorker.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getActivity(),"sing in error",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(mainIntent);
                            getActivity().finish();
                        }

                    }
                });
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slide_out_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

}