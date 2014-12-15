package com.dailyselfie;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.dailyselfie.image.ImageFactory;

public class SelfieViewActivity extends Activity {

	
	private ImageFactory factory;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selfie_view);
		
		factory = new ImageFactory(getApplicationContext());
		
		ImageView view = (ImageView)findViewById(R.id.largeImage);
		String imageName = getIntent().getExtras().getString("image");
		File file = factory.getFile(imageName);
		if(file == null){
			Toast.makeText(getApplicationContext(), "File not found!", Toast.LENGTH_LONG).show();
			finish();
		}
		Bitmap bitmap = factory.getImage(file.getAbsolutePath());
		view.setImageBitmap(bitmap);
		setTitle(imageName);
		
	}
	
}
