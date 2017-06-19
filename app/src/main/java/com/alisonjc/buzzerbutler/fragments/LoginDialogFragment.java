package com.alisonjc.buzzerbutler.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.activities.CameraActivity;

public class LoginDialogFragment extends DialogFragment {

    private SharedPreferences mSharedPreferences;
    public static final String PREFS_FILE = "MyPrefsFile";

    public static LoginDialogFragment newInstance() {
        return new LoginDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_dialog, container, false);

        mSharedPreferences = getContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        getDialog().setCanceledOnTouchOutside(false);
        Button mLoginButton = (Button) v.findViewById(R.id.LoginButton);
        EditText userName = (EditText) v.findViewById(R.id.pass);
        EditText email = (EditText) v.findViewById(R.id.email);

        mLoginButton.setVisibility(View.VISIBLE);

        if (mSharedPreferences.contains("email")) {
            mLoginButton.setText(R.string.login);
        } else {
            mLoginButton.setText(R.string.register);
        }

        mLoginButton.setOnClickListener(view -> {

            mSharedPreferences.edit().putString("email", email.toString()).apply();
            mSharedPreferences.edit().putString("pass", userName.toString()).apply();

            // Start the camera login activity.
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            startActivity(intent);

        });

        return v;
    }

}

