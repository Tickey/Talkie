package com.tickey.app.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tickey.app.R;
import com.tickey.app.view.custom.RoundedImageView;

public class NewViewHolder extends RecyclerView.ViewHolder {

	public TextView message;
	public TextView owner;
	public TextView date;
	public TextView likeCount;
	public NetworkImageView image;
	public RoundedImageView ownerAvatar;

	public NewViewHolder(View itemLayoutView) {
		super(itemLayoutView);

		message = (TextView) itemLayoutView.findViewById(android.R.id.text1);
		date = null;
		owner = (TextView) itemLayoutView.findViewById(R.id.owner);
		likeCount = (TextView) itemLayoutView.findViewById(R.id.likes);
		image = (NetworkImageView) itemLayoutView.findViewById(R.id.image);
		ownerAvatar = (RoundedImageView) itemLayoutView.findViewById(R.id.avatar);
	}
}
