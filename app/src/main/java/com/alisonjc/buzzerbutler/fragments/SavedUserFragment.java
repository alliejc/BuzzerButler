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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alisonjc.buzzerbutler.CustomRecyclerViewAdapter;
import com.alisonjc.buzzerbutler.R;
import com.alisonjc.buzzerbutler.UserItem;
import com.alisonjc.buzzerbutler.helpers.RecyclerDivider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

//TODO: Get Saved Users from DB, replace mDummyList
public class SavedUserFragment extends Fragment {

    private OnSavedUserInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private CustomRecyclerViewAdapter mAdapter;
    private ImageButton mDeleteButton;
    private static List<UserItem> mList;

    public SavedUserFragment() {
        // Required empty public constructor
    }

    public static SavedUserFragment newInstance() {
        return new SavedUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_users, container, false);
        mDeleteButton = (ImageButton) view.findViewById(R.id.delete_button);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geData();
        setRecyclerView();
    }

    private void setRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CustomRecyclerViewAdapter(mList, getContext(), index -> {
            mList.remove(index);
        });

        mRecyclerView.setAdapter(mAdapter);

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new RecyclerDivider(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
//        mAdapter.updateAdapter(mList);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onSavedUserInteraction();
        }
    }

    public List<UserItem> geData(){
        if (mList == null) {
            String json = null;
            try {
                InputStream is = getActivity().getAssets().open("dummy.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserItem>>() {
            }.getType();
            mList = gson.fromJson(json, type);

            for (UserItem userItem : mList) {
                Log.d("List", mList.toString());
                Log.i("WUser Details", userItem.getName() + userItem.getEmail());
            }
        }
        return mList;
    }

    public void addItem(UserItem item){
        mList.add(item);
//        mAdapter.updateAdapter(mList);
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSavedUserInteractionListener) {
            mListener = (OnSavedUserInteractionListener) context;
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

    public interface OnSavedUserInteractionListener {
        void onSavedUserInteraction();
    }
}
