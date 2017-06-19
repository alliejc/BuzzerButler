package com.alisonjc.buzzerbutler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    private List mList;
    private Context mContext;

    public CustomRecyclerViewAdapter(List list, Context context) {
        this.mList = list;
        this.mContext = context;
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

        holder.headerText.setText(mList.get(0).toString());
        holder.subText1.setText(mList.get(1).toString());
        holder.subText2.setText(mList.get(2).toString());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView headerText;
        public TextView subText1;
        public TextView subText2;
        public TextView subText3;

        public CustomViewHolder(View itemView) {
            super(itemView);

            headerText = (TextView) itemView.findViewById(R.id.header_text);
            subText1 = (TextView) itemView.findViewById(R.id.sub_text1);
            subText2 = (TextView) itemView.findViewById(R.id.sub_text2);
            subText3 = (TextView) itemView.findViewById(R.id.sub_text3);



        }
    }
}