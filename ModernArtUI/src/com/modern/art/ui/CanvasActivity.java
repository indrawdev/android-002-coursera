package com.modern.art.ui;

import course.lab.art.R;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CanvasActivity extends ActionBarActivity {
	
	private SeekBar mSeekbar;
	private TextView tv1, tv2, tv3, tv4, tv5;
	private int mColor1, mColor2, mColor3, mColor4, mColor5;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canvas);
		
		tv1 = (TextView) this.findViewById(R.id.textView1);		
		tv2 = (TextView) this.findViewById(R.id.textView2);		
		tv3 = (TextView) this.findViewById(R.id.textView3);		
		tv4 = (TextView) this.findViewById(R.id.textView4);		
		tv5 = (TextView) this.findViewById(R.id.textView5);		
		
		mColor1 =  ((ColorDrawable)tv1.getBackground()).getColor();
		mColor2 =  ((ColorDrawable)tv2.getBackground()).getColor();
		mColor3 =  ((ColorDrawable)tv3.getBackground()).getColor();
		mColor4 =  ((ColorDrawable)tv4.getBackground()).getColor();		
		mColor5 =  ((ColorDrawable)tv5.getBackground()).getColor();
		
		
		
		mSeekbar = (SeekBar) this.findViewById(R.id.seekBar1);
		
		mSeekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				tv1.setBackgroundColor(mColor1 | progress);
				tv2.setBackgroundColor(mColor2 | progress);
				tv3.setBackgroundColor(mColor3 | progress);
				tv4.setBackgroundColor(mColor4 | progress);
				tv5.setBackgroundColor(mColor5 | progress);
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.canvas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(CanvasActivity.this);
		
		customBuilder.setTitle("Inspired by the work of artists such as Piet Mondrain and Ben Nicholeson")
		.setMessage("Click below to learn more")		
		.setNegativeButton("Visit MOMA", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("CanvasActivity", "Visit MOMA");
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.moma.org"));
				startActivity(browserIntent);				
			}
		})
		.setPositiveButton("Not Now", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		}).create().show();
		
		return super.onOptionsItemSelected(item);
	}
}
