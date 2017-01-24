package com.alisonjc.compmusicplayer.tracks;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alisonjc.compmusicplayer.R;
import com.alisonjc.compmusicplayer.databinding.RecyclerItemModel;
import com.alisonjc.compmusicplayer.databinding.RecyclerviewItemBinding;
import com.alisonjc.compmusicplayer.spotify.TrackItem;
import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

public class TracksRecyclerAdapter<T> extends RecyclerView.Adapter<TracksRecyclerAdapter<T>.GenericViewHolder> {

    private List<TrackItem> mList;
    private Context mContext;
    private OnItemClickListener mListener;
    private int selectedItem = -1;
    private static final String TAG = "TracksRecyclerAdapter";

    public interface OnItemClickListener {
        void onItemClick(Object item, int position);
    }

    public TracksRecyclerAdapter(List<TrackItem> list, Context context, OnItemClickListener listener) {
        mList = list;
        mContext = context;
        mListener = listener;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        RecyclerviewItemBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.recyclerview_item, parent, false);

        return new GenericViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder holder, final int position) {
        TrackItem item = mList.get(position);
        RecyclerItemModel recyclerItemModel = new RecyclerItemModel(item);

        holder.bindItem(recyclerItemModel);
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

    public void updateAdapter(List<TrackItem> items) {

        mList.addAll(items);
        notifyDataSetChanged();
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewItemBinding mRecyclerviewItemBinding;

        public GenericViewHolder(RecyclerviewItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.mRecyclerviewItemBinding = itemBinding;
        }

        public void bindItem(RecyclerItemModel recyclerItemModel) {
            mRecyclerviewItemBinding.setVariable(BR.recycler_view_item, recyclerItemModel);
            mRecyclerviewItemBinding.executePendingBindings();
        }

        public void bind(TrackItem item, final TracksRecyclerAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(view -> {
                Log.i(TAG, "itemView OnClick");

                listener.onItemClick(item, getAdapterPosition());
                recyclerViewSelector(getLayoutPosition());
            });
        }
    }
}
