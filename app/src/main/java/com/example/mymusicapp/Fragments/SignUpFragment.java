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
import android.util.Log;
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

public class SignUpFragment extends Fragment {
    private TextView alreadyHaveAnAccount;
    private FrameLayout frameLayout;
    private EditText username_signup;
    private EditText email_signup;
    private EditText password_signup;
    private EditText confirmpass_signup;
    private Button signup_button;
    private ProgressBar signup_progressBar;
    private Drawable errorIcon;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.already_have_account);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);

        username_signup = view.findViewById(R.id.username_signup);
        email_signup = view.findViewById(R.id.email_signup);
        password_signup = view.findViewById(R.id.password_signup);
        confirmpass_signup = view.findViewById(R.id.confirmpass_signup);
        signup_button = view.findViewById(R.id.signup_button);
        signup_progressBar = view.findViewById(R.id.signup_progressBar);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

        username_signup.addTextChangedListener(new TextWatcher() {
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

        email_signup.addTextChangedListener(new TextWatcher() {
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

        password_signup.addTextChangedListener(new TextWatcher() {
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

        confirmpass_signup.addTextChangedListener(new TextWatcher() {
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

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_button.setEnabled(false);
                signup_button.setTextColor(getResources().getColor(R.color.transwhite));
                signupWithFirebase();
            }
        });
    }

    private void signupWithFirebase() {
        if (email_signup.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            if (password_signup.getText().toString().trim().equals(confirmpass_signup.getText().toString().trim())) {
                signup_progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email_signup.getText().toString().trim(), password_signup.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signup_progressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    signup_button.setEnabled(true);
                                    signup_button.setTextColor(getResources().getColor(R.color.white));
                                }
                            }
                        });
            } else {
                confirmpass_signup.setError("Password doesn't match.", errorIcon);
                signup_button.setEnabled(true);
                signup_button.setTextColor(getResources().getColor(R.color.white));
            }
        } else {
            email_signup.setError("Invalid Email Pattern!", errorIcon);
            signup_button.setEnabled(true);
            signup_button.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!username_signup.getText().toString().isEmpty()) {
            if (!email_signup.getText().toString().isEmpty()) {
                if (!password_signup.getText().toString().isEmpty() && password_signup.getText().toString().length() >= 6) {
                    if (!confirmpass_signup.getText().toString().isEmpty()) {
                        signup_button.setEnabled(true);
                        signup_button.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        signup_button.setEnabled(false);
                        signup_button.setTextColor(getResources().getColor(R.color.transwhite));
                    }
                } else {
                    signup_button.setEnabled(false);
                    signup_button.setTextColor(getResources().getColor(R.color.transwhite));
                }
            } else {
                signup_button.setEnabled(false);
                signup_button.setTextColor(getResources().getColor(R.color.transwhite));
            }
        } else {
            signup_button.setEnabled(false);
            signup_button.setTextColor(getResources().getColor(R.color.transwhite));
        }
    }
}