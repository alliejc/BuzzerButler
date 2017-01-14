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

    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "comp-music-player-login://callback";
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;

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

                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
                builder.setScopes(new String[]{"playlist-read-private", "playlist-read-collaborative", "playlist-modify-public", "playlist-modify-private", "streaming",
                        "user-follow-modify", "user-follow-read", "user-library-read", "user-library-modify", "user-read-private", "user-read-birthdate", "user-read-email"});
//                builder.setShowDialog(true);
                AuthenticationRequest request = builder.build();
                AuthenticationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);

                onDestroyView();
        });

        return v;
    }

}

