package luis.mayorga.dailyselfie;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoader {

    public void load(String imagePath, ImageView imageView) {
    	if (cancelPotentialLoad(imagePath, imageView)) {
            BitmapLoaderTask task = new BitmapLoaderTask(imageView);
            PreLoadedDrawable downloadedDrawable = new PreLoadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(imagePath);
        }
    }
    
    private static boolean cancelPotentialLoad(String url, ImageView imageView) {
        BitmapLoaderTask bitmapDownloaderTask = getBitmapLoaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.imagePath;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same image path is already being loaded.
                return false;
            }
        }
        return true;
    }
    
    private static BitmapLoaderTask getBitmapLoaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof PreLoadedDrawable) {
                PreLoadedDrawable downloadedDrawable = (PreLoadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

	static class PreLoadedDrawable extends ColorDrawable {
	    private final WeakReference<BitmapLoaderTask> bitmapDownloaderTaskReference;
	
	    public PreLoadedDrawable(BitmapLoaderTask bitmapDownloaderTask) {
	        super(Color.WHITE);
	        bitmapDownloaderTaskReference =
	            new WeakReference<BitmapLoaderTask>(bitmapDownloaderTask);
	    }
	
	    public BitmapLoaderTask getBitmapDownloaderTask() {
	        return bitmapDownloaderTaskReference.get();
	    }
	}

	class BitmapLoaderTask extends AsyncTask<String, Void, Bitmap> {
	    private String imagePath;
	    private final WeakReference<ImageView> imageViewReference;
	
	    public BitmapLoaderTask(ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }
	
	    @Override
	    // Actual load method, run in the task thread
	    protected Bitmap doInBackground(String... params) {
	         // params comes from the execute() call: params[0] is the image path.
	    	return ImageHelper.getScaledImageFromPath(100, params[0]);
	    }
	
	    @Override
	    // Once the image is loaded, associates it to the imageView
	    protected void onPostExecute(Bitmap bitmap) {
	    	if (imageViewReference != null) {
	    	    ImageView imageView = imageViewReference.get();
	    	    BitmapLoaderTask bitmapDownloaderTask = getBitmapLoaderTask(imageView);
	    	    // Change bitmap only if this process is still associated with it
	    	    if (this == bitmapDownloaderTask) {
	    	        imageView.setImageBitmap(bitmap);
	    	    }
	    	}
	    }
	}
}