package luis.mayorga.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewer extends Activity {
	static final String TAG = "DailySelfie-ImageViewer";
	private ImageView mFullImage = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);

		mFullImage = (ImageView) findViewById(R.id.fullImage);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
	    if(hasFocus){
	    	Intent mainIntent = getIntent();
			String imagePath = mainIntent.getStringExtra(MainActivity.IMAGE_PATH_KEY);
			int width = mFullImage.getWidth();
			int height = mFullImage.getHeight();
			Bitmap bmImg = ImageHelper.getScaledImageFromPath(width, height, imagePath);
			mFullImage.setImageBitmap(bmImg);
	    }
	}
}
