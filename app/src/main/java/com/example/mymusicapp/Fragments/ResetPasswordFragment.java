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

public class ResetPasswordFragment extends Fragment {
    private TextView back;
    private FrameLayout frameLayout;
    private EditText email_reset_pass;
    private ProgressBar reset_pass_progress;
    private TextView response_message;
    private Button reset_pass_button;
    private Drawable errorIcon;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        back = view.findViewById(R.id.back);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);

        email_reset_pass = view.findViewById(R.id.email_reset_pass);
        reset_pass_progress = view.findViewById(R.id.reset_password_progressBar);
        response_message = view.findViewById(R.id.response_message);
        reset_pass_button = view.findViewById(R.id.reset_password_button);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

        email_reset_pass.addTextChangedListener(new TextWatcher() {
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

        reset_pass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        if (email_reset_pass.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            reset_pass_progress.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email_reset_pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        response_message.setText("Check your Email.");
                        response_message.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        response_message.setText("There is an issue sending Email.");
                        response_message.setTextColor(getResources().getColor(R.color.red));
                    }
                    reset_pass_progress.setVisibility(View.INVISIBLE);
                    response_message.setVisibility(View.VISIBLE);
                }
            });
        } else {
            email_reset_pass.setError("Invalid Email Pattern!", errorIcon);
            reset_pass_button.setEnabled(true);
            reset_pass_button.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void checkInputs() {
        if (!email_reset_pass.getText().toString().isEmpty()) {
            reset_pass_button.setEnabled(true);
            reset_pass_button.setTextColor(getResources().getColor(R.color.white));
        } else {
            reset_pass_button.setEnabled(false);
            reset_pass_button.setTextColor(getResources().getColor(R.color.transwhite));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}