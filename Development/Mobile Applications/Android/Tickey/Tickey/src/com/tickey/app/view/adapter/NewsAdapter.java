package com.tickey.app.view.adapter;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tickey.app.data.model.New;
import com.tickey.app.view.viewholder.NewViewHolder;

public class NewsAdapter extends RecyclerView.Adapter<NewViewHolder> {

	private ArrayList<New> mNewsData;

	public NewsAdapter(ArrayList<New> itemsData) {
		this.mNewsData = itemsData;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public NewViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		// create a new view
		/*
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_layout, null);
*/
		// create ViewHolder

		NewViewHolder viewHolder = new NewViewHolder(parent);
		return viewHolder;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(NewViewHolder viewHolder, int position) {

		// - get data from your itemsData at this position
		// - replace the contents of the view with that itemsData
/*
		viewHolder.txtViewTitle.setText(mNewsData[position].getTitle());
		viewHolder.imgViewIcon.setImageResource(mNewsData[position]
				.getImageUrl());
		*/

	}

	// Return the size of your itemsData (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mNewsData.size();
	}
}
