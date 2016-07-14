package com.example.alisonjc.compplayertwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.Item;

import java.util.List;

public class PlaylistTracksAdapter extends ArrayAdapter<Item> {

    private final LayoutInflater mInflater;

    public PlaylistTracksAdapter(Context context, int textViewResourceId, List<Item> objects) {

        super(context, textViewResourceId, objects);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = this.mInflater.inflate(R.layout.item_track, parent, false);
        }

        Item i = this.getItem(position);

        if (i != null) {

            TextView sn = (TextView) v.findViewById(R.id.songname);
            TextView an = (TextView) v.findViewById(R.id.artistname);

            if (sn != null) {
                sn.setText(i.getTrack().getName());
            }
            if (an != null) {
                an.setText(i.getTrack().getArtists().get(0).getName());
            }

        }
        return v;
    }

}