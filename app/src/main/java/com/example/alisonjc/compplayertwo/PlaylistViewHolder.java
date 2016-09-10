package com.example.alisonjc.compplayertwo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.spotify.model.playlists.Item;


public class PlaylistViewHolder extends RecyclerView.ViewHolder {

    protected TextView playlistTitle;
    public View mView;
    public Item currentPlaylistItem;

    public PlaylistViewHolder(View itemView) {
        super(itemView);

    this.playlistTitle = (TextView) itemView.findViewById(R.id.playlisttitle);

        mView = itemView;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

public void bindViewHolder(Item item){
    currentPlaylistItem = item;
}
}
