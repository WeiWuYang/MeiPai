package com.example.Adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.example.Model.Videos;
import com.example.meipai.R;
import com.example.Adapter.CircleTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	Context context;
	List<Videos> videos;
	LayoutInflater layout;

	public MyAdapter(List<Videos> videos, Context context) {
		super();
		this.videos = videos;
		layout = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = layout.inflate(R.layout.show_item, null);
		ImageView masonry_item_img = (ImageView) view
				.findViewById(R.id.masonry_item_img);
		ImageView img_tx = (ImageView) view.findViewById(R.id.img_tx);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		Picasso.with(context).load(videos.get(position).getAvatar())
				.transform(new CircleTransform()).into(img_tx);

		Picasso.with(context).load(videos.get(position).getCover_pic())
				.into(masonry_item_img);
		tv_name.setText(videos.get(position).getScreen_name());

		return view;

	}

}
