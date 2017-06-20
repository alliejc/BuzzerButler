package com.alisonjc.buzzerbutler.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.activities.CameraActivity;
import com.alisonjc.buzzerbutler.activities.MainActivity;

public class LoginDialogFragment extends DialogFragment {

    private SharedPreferences mSharedPreferences;
    public static final String PREFS_FILE = "MyPrefsFile";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private OnCompleteListener mListener;

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
        EditText password = (EditText) v.findViewById(R.id.pass_login);
        EditText email = (EditText) v.findViewById(R.id.email_login);
        EditText name = (EditText) v.findViewById(R.id.name_login);
        EditText phoneNumber = (EditText) v.findViewById(R.id.phone_number_login);

        if (mSharedPreferences.getAll().containsKey("email") && mSharedPreferences.getAll().containsKey("pass")) {
            mLoginButton.setText(R.string.login);
            name.setVisibility(View.GONE);
            phoneNumber.setVisibility(View.GONE);

            mLoginButton.setOnClickListener(view -> {

                // Start the camera login activity.
//                Intent intent = new Intent(getActivity(), CameraActivity.class);
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);
//                startActivity(intent);

                    onDestroyView();
                    mListener.onComplete();
            });
        } else {
            mLoginButton.setText(R.string.register);

            mLoginButton.setOnClickListener(view -> {
                mSharedPreferences.edit().putString("email", email.getText().toString()).apply();
                mSharedPreferences.edit().putString("pass", password.getText().toString()).apply();
                mSharedPreferences.edit().putString("name", name.getText().toString()).apply();
                mSharedPreferences.edit().putString("phone_number", phoneNumber.getText().toString()).apply();

                // Start the camera login activity.
//                Intent intent = new Intent(getActivity(), CameraActivity.class);
//                startActivity(intent);

                onDestroyView();
                mListener.onComplete();

            });
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SavedUserFragment.OnSavedUserInteractionListener) {
            mListener = (LoginDialogFragment.OnCompleteListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCompleteListener {
        void onComplete();
    }

}

