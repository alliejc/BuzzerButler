package com.alisonjc.compmusicplayer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginDialogFrag extends DialogFragment {

    static LoginDialogFrag newInstance() {
        return new LoginDialogFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_dialog, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        View mLoginButton = v.findViewById(R.id.spotifyLoginButton);
        mLoginButton.setVisibility(View.VISIBLE);
        mLoginButton.setOnClickListener(view ->  {

            //TODO: Add login

                onDestroyView();
        });

        return v;
    }

}

