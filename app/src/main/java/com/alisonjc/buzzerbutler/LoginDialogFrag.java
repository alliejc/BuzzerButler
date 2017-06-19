package com.alisonjc.buzzerbutler;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginDialogFrag extends DialogFragment {

    static LoginDialogFrag newInstance() {
        return new LoginDialogFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_dialog, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        View mLoginButton = v.findViewById(R.id.LoginButton);
        mLoginButton.setVisibility(View.VISIBLE);
        mLoginButton.setOnClickListener(view ->  {

            //TODO: Add login

                onDestroyView();
        });

        return v;
    }

}

