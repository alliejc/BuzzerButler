package com.alisonjc.buzzerbutler;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginDialogFrag extends DialogFragment {

    private SharedPreferences mSharedPreferences;
    public static final String PREFS_FILE = "MyPrefsFile";

    static LoginDialogFrag newInstance() {
        return new LoginDialogFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_dialog, container, false);

        mSharedPreferences = getContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        getDialog().setCanceledOnTouchOutside(false);
        Button mLoginButton = (Button) v.findViewById(R.id.LoginButton);
        EditText userName = (EditText) v.findViewById(R.id.username);
        EditText email = (EditText) v.findViewById(R.id.email);

        mLoginButton.setVisibility(View.VISIBLE);

        if(mSharedPreferences.contains("username") && mSharedPreferences.contains("email")){
            mLoginButton.setText("Login");
        } else {
            mLoginButton.setText("Register");
        }

        mLoginButton.setOnClickListener(view ->  {

            mSharedPreferences.edit().putString("username", userName.toString()).apply();
            mSharedPreferences.edit().putString("email", email.toString()).apply();

                onDestroyView();
        });

        return v;
    }

}

