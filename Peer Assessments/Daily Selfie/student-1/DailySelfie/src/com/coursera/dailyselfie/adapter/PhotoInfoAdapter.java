package com.coursera.dailyselfie.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursera.dailyselfie.R;
import com.coursera.dailyselfie.entity.PhotoInfo;

public class PhotoInfoAdapter extends ArrayAdapter<PhotoInfo>{
	private final Activity context;
	private final List<PhotoInfo> photoInfo;

	static class ViewHolder {
		public ImageView image;
		public TextView text;
	}

	public PhotoInfoAdapter(Activity context, List<PhotoInfo> photoInfo) {
		super(context, R.layout.list_item, photoInfo);
		this.photoInfo = photoInfo;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.item_label);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.item_thumbnail);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		PhotoInfo photographInfo = photoInfo.get(position);
		holder.text.setText(photographInfo.getPhotoLabel());
		holder.image.setImageBitmap(photographInfo.getIcon());
		
		return rowView;
	}
	
	public PhotoInfo get(int position){
		return photoInfo.get(position);
	}	
}
