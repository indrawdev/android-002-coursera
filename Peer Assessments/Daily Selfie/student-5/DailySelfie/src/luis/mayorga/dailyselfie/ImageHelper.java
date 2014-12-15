package luis.mayorga.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class ImageHelper {
	static final String TAG = "DailySelfie-ImageHelper";
	private static String mLastPicturePath = null;
	private static String mLastPictureName = null;
	
	public static String getLastPicturePath(){
		return mLastPicturePath; 	
	}
	
	public static String getLastPictureName(){
		return mLastPictureName; 	
	}
	
	@SuppressLint("SimpleDateFormat")
	public static File createImageFile(File storageDir) throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = timeStamp + "_";
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mLastPicturePath = image.getAbsolutePath();
	    mLastPictureName = image.getName();
	    Log.i(TAG, "An initial image was created in: " + mLastPicturePath);
	    	    
	    return image;
	}
	
	public static void addImageToGallery(Context context){
		// Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(context, new String[] { mLastPicturePath }, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i(TAG, "The MediaScanner has just scanned the file: " + path);
            }
        });
	}
	
	public static void deleteInitialImage(){
		Log.i(TAG, "Take Picture intent has not returned with RESULT_OK, then we have to delete the initial image file");
		try {
			if (mLastPicturePath != null) {
		        File file = new File(mLastPicturePath);
		        if(file.exists()){
		        	file.delete();
		        	Log.i(TAG, "The initial image file was successfully deleted");
		        } else{
		        	Log.i(TAG, "The file does not exist, hence it cannot be deleted");
		        }
		    }else{
		    	Log.i(TAG, "The path of the initial file is null, hence no such file exists");
		    }
		} catch (Exception e) {
			Log.i(TAG, "There was an error when trying to delete the initial image file");	
		}
	}
	
	public static Bitmap getScaledImageFromPath(int squaredSize, String imagePath) {
		return getScaledImageFromPath(squaredSize,  squaredSize, imagePath);
	}
	
	public static Bitmap getScaledImageFromPath(int targetWidth, int targetHeight, String imagePath) {
		Log.i(TAG, "Target width is " + String.valueOf(targetWidth) + ", Target height is " + String.valueOf(targetHeight));
		
		// Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imagePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    Log.i(TAG, "Bitmap width is " + String.valueOf(photoW) + ", Bitmap height is " + String.valueOf(photoH));

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetWidth, photoH/targetHeight);
	    Log.i(TAG, "Width scale factor is " + String.valueOf(photoW/targetWidth));
	    Log.i(TAG, "Height scale factor is " + String.valueOf(photoH/targetHeight));
	    Log.i(TAG, "Min scale factor is " + String.valueOf(scaleFactor));
	    if(scaleFactor == 1) {
	    	scaleFactor = 2;
	    }

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
	    return bitmap;
	}
	
	public static void deleteAllImages(File storageDir){
		try {
			File filesList[] = storageDir.listFiles();
	    	Log.i(TAG, "There are " + filesList.length + " files inside the folder");
	    	for (int i=0; i < filesList.length; i++)
	    	{
	    		String fileName = filesList[i].getName();
	    		filesList[i].delete();
	    		Log.i(TAG,  fileName + " was successfully deleted");
	    	}
		} catch (Exception e) {
			Log.i(TAG,  "There was an error while trying to delete all images");
		}
	}
}
