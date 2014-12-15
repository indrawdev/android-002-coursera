package com.dailyselfie;

import java.io.File;
import java.io.IOException;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dailyselfie.adapter.ImageRecord;
import com.dailyselfie.adapter.ImageViewAdapter;
import com.dailyselfie.image.ImageFactory;
import com.dailyselfie.notification.SelfieNotificationReceiver;

public class DailySelfieActivity extends ListActivity {

	private ImageViewAdapter mAdapter;
	private ImageFactory factory;
	
	private AlarmManager mAlarmManager;
	private Intent mNotificationIntent;
	private PendingIntent mNotificationPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_selfie_main);
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getApplicationContext(),
					"External Storage is not available.", Toast.LENGTH_LONG)
					.show();
			finish();
		}
		
		mAdapter = new ImageViewAdapter(getApplicationContext());
		factory = new ImageFactory(getApplicationContext());
		//load initial bitmap images
		mAdapter.addAll(factory.loadFiles());
		setupNotification();
		
		ListView listView = getListView();
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
		        ImageRecord record = (ImageRecord)mAdapter.getItem(position);
			    Intent intent = new Intent(DailySelfieActivity.this, SelfieViewActivity.class)
			    						.putExtra("image", record.getImageName());
		        startActivity(intent);
				
			}
		
		});
		
		registerForContextMenu(listView);
		setListAdapter(mAdapter);
		
	}
	
	private void setupNotification() {
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		mNotificationIntent = new Intent(DailySelfieActivity.this,
				SelfieNotificationReceiver.class);
		mNotificationPendingIntent = PendingIntent.getBroadcast(
				DailySelfieActivity.this, 0, mNotificationIntent, 0);
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
				INITIAL_ALARM_DELAY,
				mNotificationPendingIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daily_selfie_main, menu);
		return true;
	}

	Button.OnClickListener mTakePicOnClickListener = 
			new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
		};
	
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			File f = factory.setUpPhotoFile();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
		startActivityForResult(takePictureIntent, 0);
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			mAdapter.add(factory.handlePhoto());
		}else if(resultCode == RESULT_CANCELED){
			factory.removeCurrentFile();
		}
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.take_photo) {
			dispatchTakePictureIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.delete:
	        	removeFile(info.position);
	            return true;
	        case R.id.delete_all:
	        	removeAllFile();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private void removeFile(int position){
		String path = mAdapter.remove(position);
		factory.removeFile(path);
	}

	private void removeAllFile(){
		mAdapter.removeAllViews();
		factory.removeAllFile();
	}
}
