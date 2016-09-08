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

        View view = convertView;

        if (view == null) {
            view = this.mInflater.inflate(R.layout.item_track, parent, false);
        }

        Item item = this.getItem(position);

        if (item != null) {

            TextView songName = (TextView) view.findViewById(R.id.songname);
            TextView artistName = (TextView) view.findViewById(R.id.artistname);

            if (songName != null) {
                songName.setText(item.getTrack().getName());
            }
            if (artistName != null) {
                artistName.setText(item.getTrack().getArtists().get(0).getName());
            }

        }
        return view;
    }

}