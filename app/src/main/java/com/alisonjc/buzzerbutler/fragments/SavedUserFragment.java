package com.alisonjc.buzzerbutler.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alisonjc.buzzerbutler.CustomRecyclerViewAdapter;
import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.helpers.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;

//TODO: Get Saved Users from DB, replace mDummyList
public class SavedUserFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private List mDummyList = new ArrayList();

    public SavedUserFragment() {
        // Required empty public constructor
    }

    public static SavedUserFragment newInstance() {
        return new SavedUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDummyList.add("John Doe");
        mDummyList.add("206-123-4567");
        mDummyList.add("1234");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();
    }

    private void setRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        CustomRecyclerViewAdapter mAdapter= new CustomRecyclerViewAdapter(mDummyList, getContext());

        mRecyclerView.setAdapter(mAdapter);

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new RecyclerDivider(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
