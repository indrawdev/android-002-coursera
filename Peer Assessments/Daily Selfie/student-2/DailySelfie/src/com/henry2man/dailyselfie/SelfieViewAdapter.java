package com.henry2man.dailyselfie;

import com.henry2man.dailyselfie.io.FileUtils;
import com.henry2man.dailyselfie.model.Selfie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SelfieViewAdapter extends BaseAdapter {

	private Context mContext;
	private SelfiesDataSource mSelfiesDataSource;

	public SelfieViewAdapter(Context mContext) {
		this.mContext = mContext;
		this.mSelfiesDataSource = new SelfiesDataSource(mContext);
	}

	@Override
	public int getCount() {
		return mSelfiesDataSource.getCount();
	}

	@Override
	public Selfie getItem(int position) {
		return mSelfiesDataSource.getSelfieByPosition(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Selfie selfie = getItem(position);

		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			int thumbWidth = (int) parent.getContext().getResources()
					.getDimension(R.dimen.grid_column_size);
			imageView.setLayoutParams(new GridView.LayoutParams(thumbWidth,
					thumbWidth));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageBitmap(ImageUtils.computeImageBitmap(imageView,
				selfie));
		return imageView;
	}

}
