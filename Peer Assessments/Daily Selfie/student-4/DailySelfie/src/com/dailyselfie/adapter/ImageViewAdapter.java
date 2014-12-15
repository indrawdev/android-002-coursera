package com.dailyselfie.adapter;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailyselfie.R;


public class ImageViewAdapter extends BaseAdapter {

	private ArrayList<ImageRecord> list = new ArrayList<ImageRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public ImageViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		ImageRecord curr = list.get(position);
		if (null == convertView || newView.getTag() == null) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.image_view, null);
			holder.image = (ImageView) newView.findViewById(R.id.image);
			holder.name = (TextView) newView.findViewById(R.id.image_name);
			
		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.image.setImageBitmap(curr.getImage());
		holder.name.setText(curr.getImageName());
		return newView;
	}
	
	public static class ViewHolder {
	
		ImageView image;
		TextView name;
		
	}

	public void add(ImageRecord item) {
		list.add(item);
		notifyDataSetChanged();
	}
	
	public void addAll(ArrayList<ImageRecord> listItem) {
		list.addAll(listItem);
		notifyDataSetChanged();
	}
	
	public ArrayList<ImageRecord> getList(){
		return list;
	}
	
	public void removeAllViews(){
		list.clear();
		notifyDataSetChanged();
	}
	
	public String remove(int position){
		ImageRecord item = (ImageRecord)getItem(position);
		list.remove(item);
		notifyDataSetChanged();
		return item.getImageName();
	}
}

