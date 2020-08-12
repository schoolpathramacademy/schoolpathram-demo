package com.schoolpathram.schoolpathramdotcom.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.model.Card;
import com.schoolpathram.schoolpathramdotcom.ui.study.CardView;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    List<Card> mData;
    Context mContext;
    int[] colors = {0xff01a3a1, 0xff91bbeb, 0xff01b1bf, 0xffff9d62, 0xff2d3867, 0xffee697e};//颜色组

    public void setData(List<Card> list) {
        this.mData = list;
    }

    public CardViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new CardView(mContext);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.content.setText(mData.get(position).getContent());
        holder.title.setText(mData.get(position).getTitle());

        holder.comment.setText(mData.get(position).getComment());
        holder.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mData.get(holder.getAdapterPosition()).setComment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //设置颜色变化
        ((CardView) holder.itemView).changeTheme(colors[position % 6]);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout top;
        ImageView center;
        TextView comment;
        TextView content;
        TextView title;


        ViewHolder(View view) {
            super(view);
            top = (RelativeLayout) view.findViewById(R.id.top);
            center = (ImageView) view.findViewById(R.id.center);
            comment = (TextView) view.findViewById(R.id.comment);
            content = (TextView) view.findViewById(R.id.content);
            title = (TextView) view.findViewById(R.id.cardTitle);

        }
    }
}