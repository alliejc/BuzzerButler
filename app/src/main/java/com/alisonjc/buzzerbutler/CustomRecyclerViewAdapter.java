package com.alisonjc.buzzerbutler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    private List<UserItem> mList;
    private Context mContext;

    private final OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int index);
    }

    public CustomRecyclerViewAdapter(List<UserItem>list, Context context, OnDeleteClickListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.recyclerview_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.headerText.setText(mList.get(position).getName());
        holder.subText1.setText(mList.get(position).getPhoneNumber());
        holder.subText2.setText(mList.get(position).getPinCode());
        holder.deleteButton.setImageResource(R.drawable.delete);

    }

    public void updateAdapter(List<UserItem> list) {

        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView headerText;
        public TextView subText1;
        public TextView subText2;
        public ImageButton deleteButton;

        public CustomViewHolder(View itemView) {
            super(itemView);

            headerText = (TextView) itemView.findViewById(R.id.header_text);
            subText1 = (TextView) itemView.findViewById(R.id.sub_text1);
            subText2 = (TextView) itemView.findViewById(R.id.sub_text2);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(v -> {
                //TODO: delete item from list
                mList.remove(getAdapterPosition());
//                listener.onDeleteClick(mList.get(getAdapterPosition()));
            });

        }
    }
}