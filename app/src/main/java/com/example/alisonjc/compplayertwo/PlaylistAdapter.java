package com.example.alisonjc.compplayertwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.spotify.model.playlists.Item;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Item> {

    private final LayoutInflater mInflater;

    public PlaylistAdapter(Context context, int textViewResourceId, List<Item> objects) {
        super(context, textViewResourceId, objects);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = this.mInflater.inflate(R.layout.item_playlist, parent, false);
        }
        Item i = getItem(position);

        if (i != null) {
            TextView pt = (TextView) v.findViewById(R.id.playlisttitle);

            if (pt != null) {
                pt.setText(i.getName());
            }
        }
        return v;
    }
}
