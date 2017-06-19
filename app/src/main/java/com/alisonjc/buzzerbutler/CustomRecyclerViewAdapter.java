package com.alisonjc.buzzerbutler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    private List mInstructionList;
    private Context mContext;

    public CustomRecyclerViewAdapter(List list, Context context) {
        this.mInstructionList = list;
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

        String exerciseItem = mInstructionList.get(position).toString();
        TextView instructionText = holder.instructionText;

        instructionText.setText(exerciseItem);
    }

    @Override
    public int getItemCount() {
        return mInstructionList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView instructionText;

        public CustomViewHolder(View itemView) {
            super(itemView);

            instructionText = (TextView) itemView.findViewById(R.id.recyclerview_header_text);
        }
    }
}