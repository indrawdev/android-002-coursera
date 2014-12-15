package com.example.myui;

import java.util.HashMap;
import java.util.LinkedList;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener, OnClickListener {
	
	private final String URL ="http://www.moma.org"; 
	private HashMap<String,Integer> originColor;
	int [] tColor = new int[4];
	private LinkedList<TextView> colorblocks;
	private Dialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);  
		
		Resources res = this.getResources();
		
		this.getActionBar().setBackgroundDrawable(res.getDrawable(R.color.cadetblue));
		colorblocks = new LinkedList<TextView>();
		colorblocks.add((TextView)this.findViewById(R.id.textview1_1));
		colorblocks.add((TextView)this.findViewById(R.id.textview1_2));
		colorblocks.add((TextView)this.findViewById(R.id.textview2_1));
		colorblocks.add((TextView)this.findViewById(R.id.textview2_3));
		
		tColor[0] = res.getColor(R.color.darkcyan);
		tColor[1] = res.getColor(R.color.indianred);
		tColor[2] = res.getColor(R.color.cadetblue);
		tColor[3] = res.getColor(R.color.mediumslateblue);
		originColor = new HashMap<String,Integer>();
		for(TextView tv:colorblocks){
			ColorDrawable cd = (ColorDrawable)tv.getBackground();
			originColor.put(Integer.toString(tv.getId()),cd.getColor());
		}
		SeekBar sb = (SeekBar) findViewById(R.id.seekBar);
		sb.setMax(100);
		sb.setOnSeekBarChangeListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if(dialog==null){
				dialog = new Dialog(MainActivity.this, R.style.MyDialog);
	            //…Ë÷√À¸µƒContentView
	            dialog.setContentView(R.layout.dialog);
	            
	            TextView visit = (TextView)dialog.findViewById(R.id.dialogText3);
	            visit.setOnClickListener(this);
	            TextView notNow = (TextView)dialog.findViewById(R.id.dialogText4);
	            notNow.setOnClickListener(this);
			}
            
            dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar sb, int progress, boolean arg2) {
		int i = 0;
		int max = sb.getMax();
		for (TextView tv:colorblocks){
			int col1 = this.originColor.get(Integer.toString(tv.getId()));
			int r1 = col1 / 0x10000;
			int r2 = tColor[i] / 0x10000;

			int b1 = (col1 / 0x100) % 0x100;
			int b2 = (tColor[i] / 0x100) % 0x100;

			int g1 = col1 % 0x100;
			int g2 = tColor[i] % 0x100;
			
			int col2 = (g1 + (g2-g1) * progress / max)
					+ (b1 + (b2-b1) * progress /max) * 0x100
					+ (r1 + (r2-r1) * progress / max) * 0x10000;
			
			tv.setBackgroundColor(col2);
			i++;
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.dialogText3){
			Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.URL));
			this.startActivity(baseIntent);
			dialog.dismiss();
		}
		else if(v.getId() == R.id.dialogText4)
			dialog.dismiss();
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
