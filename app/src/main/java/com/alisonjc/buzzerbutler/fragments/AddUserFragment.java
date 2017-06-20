package com.alisonjc.buzzerbutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.UserItem;

import java.util.ArrayList;
import java.util.List;


//TODO:Save users to external DB
public class AddUserFragment extends Fragment {

    private OnAddUserInteraction mListener;
    private Button mSaveButton;
    private EditText mName;
    private EditText mPhoneNumber;
    private EditText mPinCode;
    private UserItem mUserItem;

    public AddUserFragment() {
        // Required empty public constructor
    }

    public static AddUserFragment newInstance() {
        AddUserFragment fragment = new AddUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_user, container, false);
        mSaveButton = (Button) v.findViewById(R.id.save_button);
        mName = (EditText) v.findViewById(R.id.name_edit_text);
        mPhoneNumber = (EditText) v.findViewById(R.id.phone_edit_text);
        mPinCode = (EditText) v.findViewById(R.id.pincode_edit_text);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSaveButton.setOnClickListener(v -> {
            mUserItem = new UserItem();
            mUserItem.setName(mName.getText().toString());
            mUserItem.setPhoneNumber(mPhoneNumber.getText().toString());
            mUserItem.setPinCode(mPinCode.getText().toString());
            mListener.onAddUserInteraction(mUserItem);
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(UserItem item) {
        if (mListener != null) {
            mListener.onAddUserInteraction(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddUserInteraction) {
            mListener = (OnAddUserInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddUserInteraction {
        void onAddUserInteraction(UserItem item);
    }
}
