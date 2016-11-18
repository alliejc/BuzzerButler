package com.example.alisonjc.compplayertwo.playlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.R;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.Item;

import java.util.List;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistRecyclerAdapter.PlaylistViewHolder> {

    private List<Item> mPlaylistItemList;
    private Context mContext;
    private final onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(Item item);
    }

    public PlaylistRecyclerAdapter(Context context, List<Item> playlistItemList, onItemClickListener listener) {

        this.mPlaylistItemList = playlistItemList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.recyclerview_item, parent, false);
        PlaylistViewHolder holder = new PlaylistViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {

        Item item = mPlaylistItemList.get(position);
        TextView playlistTitle = holder.playlistTitle;
        TextView songCount = holder.songCount;

        playlistTitle.setText(item.getName());
        songCount.setText(item.getTracks().getTotal().toString() + " songs");
        holder.bind(item, listener);
    }

    public void updateAdapter(List<Item> items) {

        mPlaylistItemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPlaylistItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        public TextView playlistTitle;
        public TextView songCount;

        public PlaylistViewHolder(View itemView) {
            super(itemView);

            playlistTitle = (TextView) itemView.findViewById(R.id.recyclerview_header_text);
            songCount = (TextView) itemView.findViewById(R.id.recyclerview_sub_text);
        }

        public void bind(final Item item, final onItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
