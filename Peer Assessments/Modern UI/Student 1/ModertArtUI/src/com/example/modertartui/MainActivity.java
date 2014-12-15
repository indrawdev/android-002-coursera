package com.example.modertartui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private SeekBar barra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		barra = (SeekBar) findViewById(R.id.barra);
		barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				TextView r1 = ((TextView) findViewById(R.id.textView1));
				r1.setBackgroundColor(Color.argb(255, 100, 149 - progress, 237 - progress));
				
				TextView r2 = ((TextView) findViewById(R.id.textView2));
				r2.setBackgroundColor(Color.argb(255, 255, 105 - progress, 180 - progress));
				
				TextView r3 = ((TextView) findViewById(R.id.textView3));
				r3.setBackgroundColor(Color.argb(255, 178, 34  + progress, 34  + progress));
				
				TextView r5 = ((TextView) findViewById(R.id.textView5));
				r5.setBackgroundColor(Color.argb(255, 0  , 0   + progress, 204 - progress));
			}
		});
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
			return true;
		}
		if (id == R.id.opcion1) { mostrarDialogo(); }
		/*switch (item.getItemId()) {
        case R.id.item1:
          Toast.makeText(this, "Option1", Toast.LENGTH_SHORT).show();
          return true;
        case R.id.item2:
          Toast.makeText(this, "Option2", Toast.LENGTH_SHORT).show();
          return true;
        default:
          return super.onOptionsItemSelected(item);
        } */
		return super.onOptionsItemSelected(item);
	}
	
	public void mostrarDialogo() {
		AlertDialog alerta = new AlertDialog.Builder(this).create();
		alerta.setMessage("Inspired by the works of artists such as Piet Mondrian and Ben Nicholson. Click below to learn more! ");
		alerta.setButton(AlertDialog.BUTTON_POSITIVE, "Visit MOMA", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.moma.org"));
				startActivity(intent);
			} 
		}); 
		alerta.setButton(AlertDialog.BUTTON_NEGATIVE, "Not Now", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		}); 
		alerta.show();
	}
}