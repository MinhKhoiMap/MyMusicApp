package com.example.mymusicapp.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicapp.Activities.MainActivity;
import com.example.mymusicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {
    private TextView dontHaveAnAccount;
    private TextView resetPassword;
    private FrameLayout frameLayout;
    private EditText email_signin;
    private EditText password_signin;
    private Button signin_button;
    private ProgressBar signin_progress;
    private Drawable errorIcon;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        dontHaveAnAccount = view.findViewById(R.id.dont_have_an_account);
        resetPassword = view.findViewById(R.id.reset_password);
        email_signin = view.findViewById(R.id.email_signin);
        password_signin = view.findViewById(R.id.password_signin);
        signin_button = view.findViewById(R.id.signin_button);
        signin_progress = view.findViewById(R.id.signin_progressBar);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ResetPasswordFragment());
            }
        });

        email_signin.addTextChangedListener(new TextWatcher() {
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

        password_signin.addTextChangedListener(new TextWatcher() {
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

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin_button.setEnabled(false);
                signin_button.setTextColor(getResources().getColor(R.color.transwhite));
                signinWithFirebase();
            }
        });
    }

    private void signinWithFirebase() {
        if (email_signin.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            signin_progress.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email_signin.getText().toString().trim(), password_signin.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            signin_progress.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                signin_button.setEnabled(true);
                                signin_button.setTextColor(getResources().getColor(R.color.white));
                            }
                        }
                    });
        } else {
            email_signin.setError("Invalid Email Pattern!", errorIcon);
            signin_button.setEnabled(true);
            signin_button.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void checkInputs() {
        if (!email_signin.getText().toString().isEmpty()) {
            if (!password_signin.getText().toString().isEmpty()) {
                signin_button.setEnabled(true);
                signin_button.setTextColor(getResources().getColor(R.color.white));
            } else {
                signin_button.setEnabled(false);
                signin_button.setTextColor(getResources().getColor(R.color.transwhite));
            }
        } else {
            signin_button.setEnabled(false);
            signin_button.setTextColor(getResources().getColor(R.color.transwhite));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_from_left);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}