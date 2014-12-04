package com.tickey.app.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NewViewHolder extends RecyclerView.ViewHolder {

    public TextView message;
    public TextView date;
    public ImageView like;
    public TextView likeCount;
    public TextView image;
     
    public NewViewHolder(View itemLayoutView) {
        super(itemLayoutView);
    }
}
