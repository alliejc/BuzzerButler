//package com.example.alisonjc.compplayertwo;
//
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//public abstract class GenericRecyclerAdapter<T> extends RecyclerView.Adapter<GenericRecyclerAdapter.GenericViewHolder> {
//
//    private List<? extends T> mList;
//    private Context mContext;
//    private OnItemClickListener mListener;
//    private int selectedItem = -1;
//    private GenericViewHolder mViewHolder;
//
//    public interface OnItemClickListener {
//        void onItemClick(Object item, int position);
//    }
//
//    public GenericRecyclerAdapter(List<T> list, Context context, OnItemClickListener listener) {
//        mList = list;
//        mContext = context;
//        mListener = listener;
//    }
//
//    @Override
//    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        mContext = parent.getContext();
//        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//        View v = layoutInflater.inflate(R.layout.item_track, parent, false);
//        mViewHolder = new GenericViewHolder(v);
//
//        return mViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(GenericViewHolder holder, int position) {
//        bindView(getItem(position), holder);
//        final Object item = mList.get(position);
//        TextView songName = holder.songName;
//        TextView artistName = holder.artistName;
//        artistName.setText("hello");
//        songName.setText("hi");
//
//        holder.itemView.setSelected(selectedItem == position);
//        holder.bind(item, mListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//    protected abstract void bindView(T item, RecyclerView.ViewHolder viewHolder);
//
//    public T getItem(int index) {
//        return ((mList != null && index < mList.size()) ? mList.get(index) : null);
//    }
//
//    public void recyclerViewSelector(int position) {
//
//        notifyItemChanged(selectedItem);
//        selectedItem = position;
//        notifyItemChanged(selectedItem);
//        notifyDataSetChanged();
//    }
//
//    public class GenericViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView songName;
//        private TextView artistName;
//
//        public GenericViewHolder(View itemView) {
//            super(itemView);
//
//            songName = (TextView) itemView.findViewById(R.id.songname);
//            artistName = (TextView) itemView.findViewById(R.id.artistname);
//        }
//
//        public void bind(final Object item, final GenericRecyclerAdapter.OnItemClickListener listener) {
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//                    listener.onItemClick(item, getAdapterPosition());
//                    notifyItemChanged(selectedItem);
//                    selectedItem = getLayoutPosition();
//                    notifyItemChanged(selectedItem);
//                    notifyDataSetChanged();
//                }
//            });
//        }
//    }
//
//
//}
