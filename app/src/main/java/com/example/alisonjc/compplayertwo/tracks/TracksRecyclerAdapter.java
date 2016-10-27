package com.example.alisonjc.compplayertwo.tracks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.R;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.Item;

import java.util.List;


public class TracksRecyclerAdapter extends RecyclerView.Adapter<TracksRecyclerAdapter.TracksViewHolder> {

    public interface onItemClickListener {
        void onItemClick(Item item, int position);
    }

    private List<Item> mTracksList;
    private Context mContext;
    private onItemClickListener mListener;
    private int selectedItem = -1;

    public TracksRecyclerAdapter(Context context, List<Item> tracksList, onItemClickListener listener) {
        mContext = context;
        mTracksList = tracksList;
        mListener = listener;
    }

    @Override
    public TracksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.item_track, parent, false);

        return new TracksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TracksViewHolder holder, int position) {

        final Item item = mTracksList.get(position);
        TextView songName = holder.songName;
        TextView artistName = holder.artistName;
        songName.setText(item.getTrack().getName());
        artistName.setText(item.getTrack().getArtists().get(0).getName());
        holder.itemView.setSelected(selectedItem == position);
        holder.onBind(item, mListener);
    }

    @Override
    public int getItemCount() {
        return mTracksList.size();
    }

    public void updateAdapter(List<Item> list) {

        mTracksList.addAll(list);
        notifyDataSetChanged();
    }

    public void recyclerViewSelector(int position) {

        notifyItemChanged(selectedItem);
        selectedItem = position;
        notifyItemChanged(selectedItem);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class TracksViewHolder extends RecyclerView.ViewHolder {

        private TextView songName;
        private TextView artistName;

        public TracksViewHolder(View itemView) {
            super(itemView);

            songName = (TextView) itemView.findViewById(R.id.songname);
            artistName = (TextView) itemView.findViewById(R.id.artistname);
        }

        public void onBind(final Item item, final TracksRecyclerAdapter.onItemClickListener listener) {
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



