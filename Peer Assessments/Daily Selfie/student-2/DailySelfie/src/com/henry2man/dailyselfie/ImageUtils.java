package com.henry2man.dailyselfie;

import com.henry2man.dailyselfie.io.FileUtils;
import com.henry2man.dailyselfie.model.Selfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageUtils {

	public static Bitmap computeImageBitmap(ImageView mImageView, Selfie selfie) {

		if (selfie != null &&  FileUtils.selfieExists(selfie)) {
			int targetW = mImageView.getLayoutParams().width;
			int targetH = mImageView.getLayoutParams().height;

			// Get the dimensions of the bitmap
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			String path = selfie.getPath();
			BitmapFactory.decodeFile(path, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			// Determine how much to scale down the image
			int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

			// Decode the image file into a Bitmap sized to fill the View
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			return BitmapFactory.decodeFile(path, bmOptions);
		} else {
			return BitmapFactory.decodeResource(mImageView.getContext()
					.getResources(), android.R.drawable.ic_dialog_alert);
		}
	}

}
