package com.alisonjc.compmusicplayer.tracks;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alisonjc.compmusicplayer.R;
import com.alisonjc.compmusicplayer.spotify.model.playlist_tracklists.Item;

import java.util.List;

public class TracksRecyclerAdapter<T> extends RecyclerView.Adapter<TracksRecyclerAdapter<T>.GenericViewHolder> {

    private List<?> mList;
    private Context mContext;
    private OnItemClickListener mListener;
    private int selectedItem = -1;
    private GenericViewHolder mViewHolder;
    private Item mPlaylistTracksItem;
    private com.alisonjc.compmusicplayer.spotify.model.UserTracks.Item mTracksItem;
    private static final String TAG = "TracksRecyclerAdapter";

    public interface OnItemClickListener {
        void onItemClick(Object item, int position);
    }

    public TracksRecyclerAdapter(List<T> list, Context context, OnItemClickListener listener) {
        mList = list;
        mContext = context;
        mListener = listener;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        mViewHolder = new GenericViewHolder(v);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder holder, final int position) {
        Object item = mList.get(position);
        TextView headerTextView = holder.headerTextView;
        TextView subTextView = holder.subTextView;

        if(item instanceof Item){
            mPlaylistTracksItem = (Item)item;
            headerTextView.setText(mPlaylistTracksItem.getTrack().getName());
            subTextView.setText(mPlaylistTracksItem.getTrack().getArtists().get(0).getName());
            Log.i(TAG, "setText PlaylistTracksItem");

        } else if (item instanceof com.alisonjc.compmusicplayer.spotify.model.UserTracks.Item){
            mTracksItem = (com.alisonjc.compmusicplayer.spotify.model.UserTracks.Item) item;
            headerTextView.setText(mTracksItem.getTrack().getName());
            subTextView.setText(mTracksItem.getTrack().getArtists().get(0).getName());
        }

        holder.bind(item, mListener);
        holder.itemView.setSelected(selectedItem == position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void recyclerViewSelector(int position) {
        Log.i(TAG, "recyclerViewSelector");

        notifyItemChanged(selectedItem);
        selectedItem = position;
        notifyItemChanged(selectedItem);


    }

    public class GenericViewHolder extends RecyclerView.ViewHolder {

        private TextView headerTextView;
        private TextView subTextView;

        public GenericViewHolder(View itemView) {
            super(itemView);

            headerTextView = (TextView) itemView.findViewById(R.id.recyclerview_header_text);
            subTextView = (TextView) itemView.findViewById(R.id.recyclerview_sub_text);
        }


        public void bind(final Object item, final TracksRecyclerAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(TAG, "itemView OnClick");

                    listener.onItemClick(item, getAdapterPosition());
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                }
            });
        }
    }


}
