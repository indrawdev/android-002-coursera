package com.coursera.dailyselfie;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class PictureActivity extends Activity {
	ImageView imageView;
	String mCurrentPhotoPath;
	DialogFragment deleteDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		
		Intent mainActivityIntent = getIntent();
		mCurrentPhotoPath = mainActivityIntent.getStringExtra(MainActivity.PHOTO_PATH);
		String photoName = mCurrentPhotoPath.replace("file:", "").replace(Environment.getExternalStoragePublicDirectory(MainActivity.GALLERY_PATH).toString() + File.separator, "");
		
		imageView = (ImageView) findViewById(R.id.picture_view);
		setTitle(photoName);
		getActionBar().setIcon(null);
		new LoadImageTask().execute(mCurrentPhotoPath);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void delete(){
		File photo = new File(mCurrentPhotoPath);
		photo.delete();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picture, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_delete) {
			deleteDialog = AlertDialogFragment.newInstance();
			deleteDialog.show(getFragmentManager(), "Confirm");
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
	     protected Bitmap doInBackground(String... photoPaths) {
	 	    // Get the dimensions of the View
	 		int targetW = 1080;
	 		int targetH = 1920;
	 		 
	 	    // Get the dimensions of the bitmap
	 	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	 	    bmOptions.inJustDecodeBounds = true;
	 	    BitmapFactory.decodeFile(photoPaths[0], bmOptions);
	 	    int photoW = bmOptions.outWidth;
	 	    int photoH = bmOptions.outHeight;

	 	    // Determine how much to scale down the image
	 	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	 	    // Decode the image file into a Bitmap sized to fill the View
	 	    bmOptions.inJustDecodeBounds = false;
	 	    bmOptions.inSampleSize = scaleFactor;
	 	    bmOptions.inPurgeable = true;

	 	    return BitmapFactory.decodeFile(photoPaths[0], bmOptions);
	     }

	     protected void onPostExecute(Bitmap result) {
	    	 imageView.setImageBitmap(result);
	     }
	 }

	public static class AlertDialogFragment extends DialogFragment {

		public static AlertDialogFragment newInstance() {
			return new AlertDialogFragment();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setMessage("Delete current photo?")
					.setCancelable(true)
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {}
							})
					.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {
								public void onClick(final DialogInterface dialog, int id) {
									((PictureActivity) getActivity()).delete();
								}
							}).create();
		}
	}
}
