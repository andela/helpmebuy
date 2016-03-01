package com.andela.helpmebuy.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.activities.ChangePasswordActivity;
import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.FirebaseAuth;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.Launcher;

public class ConfirmPasswordFragment extends Fragment {

    public static final String CONFIRM_PASSWORD = "confirmPassword";
    private EditText confirmPassword;

    private Button confirmPasswordButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmpassword_fragment, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(final View view) {
        confirmPassword = (EditText) view.findViewById(R.id.confirm_password);

        confirmPasswordButton = (Button) view.findViewById(R.id.confirm_password_button);
        confirmPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPasswordButton.setEnabled(false);
                confirmPassword(confirmPassword.getText().toString());
            }
        });
    }

    private void confirmPassword(String password){
        User user = CurrentUserManager.get(getContext());

        FirebaseAuth firebaseAuth = new FirebaseAuth(getContext());
        firebaseAuth.signIn(user.getEmail(), password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                confirmPasswordButton.setEnabled(true);
                Bundle bundle = new Bundle();
                bundle.putBoolean(CONFIRM_PASSWORD, true);
                Launcher.launchActivity(getContext(), bundle, ChangePasswordActivity.class);
                getActivity().finish();
            }

            @Override
            public void onCancel() {
                confirmPasswordButton.setEnabled(true);
            }

            @Override
            public void onError(String errorMessage) {
                confirmPasswordButton.setEnabled(true);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                confirmPasswordButton.setEnabled(true);
                Toast.makeText(getContext(), "Error, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
