package com.example.alisonjc.compplayertwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alisonjc.compplayertwo.spotify.model.playlists.Item;

import java.util.List;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    private List<Item> mPlaylistItemList;
    private Context mContext;
    private int focusedItem = 0;

    public PlaylistRecyclerAdapter(Context context, List<Item> playlistItemList){
        this.mPlaylistItemList = playlistItemList;
        this.mContext = context;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, null);

        //viewHolder instance to hold references
        PlaylistViewHolder holder = new PlaylistViewHolder(view);

        //returned back to the layoutManager
        return holder;
        }

    //called by LayoutManager to get data for specific row position and data
    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {

        Item playlistItemPosition = mPlaylistItemList.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();

    }

    public void clearAdapter(){
        mPlaylistItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != mPlaylistItemList ? mPlaylistItemList.size(): 0);
    }

}
