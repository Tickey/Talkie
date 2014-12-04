package com.tickey.app.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.tickey.app.R;
import com.tickey.app.common.BaseApplication;
import com.tickey.app.data.model.New;
import com.tickey.app.view.viewholder.NewViewHolder;

public class NewsAdapter extends RecyclerView.Adapter<NewViewHolder> {

	private ArrayList<New> mNewsData;
	private ImageLoader mImageLoader = BaseApplication.getInstance()
			.getImageLoader();
	private Context mContext;

	public NewsAdapter(ArrayList<New> itemsData) {
		this.mNewsData = itemsData;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		mContext = parent.getContext();
		View itemLayoutView = LayoutInflater.from(mContext).inflate(
				R.layout.list_item_new, parent, false);
		// create ViewHolder

		NewViewHolder viewHolder = new NewViewHolder(itemLayoutView);
		return viewHolder;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(NewViewHolder viewHolder, int position) {

		// - get data from your itemsData at this position
		// - replace the contents of the view with that itemsData
		
		New newData = mNewsData.get(position);
		viewHolder.image.setImageUrl(newData.postImageUrl, mImageLoader);
		viewHolder.likeCount.setText(mContext.getResources().getQuantityString(R.plurals.likes, newData.likes));
		viewHolder.message.setText(newData.shortMessage);
		viewHolder.owner.setText(newData.username);
		viewHolder.ownerAvatar.setImageUrl(newData.ownerAvatarUrl, mImageLoader);
		

	}

	// Return the size of your itemsData (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mNewsData.size();
	}
}
