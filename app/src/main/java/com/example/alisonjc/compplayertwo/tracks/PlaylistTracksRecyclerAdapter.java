package com.example.alisonjc.compplayertwo.tracks;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.R;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.Item;

import java.util.List;

public class PlaylistTracksRecyclerAdapter extends RecyclerView.Adapter<PlaylistTracksRecyclerAdapter.PlaylistTracksViewHolder> {

    private List<Item> mPlaylistTracksList;
    private Context mContext;
    private final OnItemClickListener listener;
    private PlaylistTracksViewHolder mPlaylistTracksViewHolder;
    private int selectedItem = 0;

    public interface OnItemClickListener {
        void onItemClick(Item item, int position);
    }

    public PlaylistTracksRecyclerAdapter(Context context, List<Item> list, OnItemClickListener listener) {
        this.mContext = context;
        this.mPlaylistTracksList = list;
        this.listener = listener;
    }

    @Override
    public PlaylistTracksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.item_track, parent, false);
        mPlaylistTracksViewHolder = new PlaylistTracksViewHolder(v);

        return mPlaylistTracksViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaylistTracksViewHolder holder, final int position) {

        final Item item = mPlaylistTracksList.get(position);
        TextView textView = holder.songName;
        textView.setText(item.getTrack().getName());
        holder.itemView.setSelected(selectedItem == position);
        holder.bind(item, listener);

    }

    public void updateAdapter(List<Item> items) {

        mPlaylistTracksList.addAll(items);
        notifyDataSetChanged();
    }


    public void recyclerViewSelector(int position) {

        notifyItemChanged(selectedItem);
        mPlaylistTracksViewHolder.itemView.setSelected(selectedItem == position);
        selectedItem = position;
        //selectedItem = mPlaylistTracksViewHolder.getLayoutPosition();
        notifyItemChanged(selectedItem);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mPlaylistTracksList.size();
    }


    public class PlaylistTracksViewHolder extends RecyclerView.ViewHolder {

        private TextView songName;

        public PlaylistTracksViewHolder(View itemView) {
            super(itemView);

            songName = (TextView) itemView.findViewById(R.id.songname);
        }

        public void bind(final Item item, final PlaylistTracksRecyclerAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onItemClick(item, getAdapterPosition());

                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    notifyDataSetChanged();


                }
            });
        }
    }
}
