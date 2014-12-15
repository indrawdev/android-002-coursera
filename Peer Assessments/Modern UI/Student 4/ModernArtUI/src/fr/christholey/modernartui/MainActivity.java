package fr.christholey.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private static final int INFORMATION_TAG = 0;
	private static final String DIALOG_INFO_WEBSITE = "Inspired by the work of artists such as Piet Mondrian and Ben Nicholson \n Click below to learn more !";
	protected static final String MOMA_WEBSITE = "http://www.moma.org/visit/calendar/exhibitions/1469";
	
	private SeekBar mSeekbar;
	private View mViewLeftUp;
	private View mViewLeftDown;
	private View mViewRightUp;
	private View mViewRightMiddle;
	private View mViewRightDown;
	
	private DialogFragment mFragmentDialog;
	
	// IDs for menu items
	private static final int MENU_MORE_INFO = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		mSeekbar = (SeekBar) findViewById(R.id.seekBar);
		mViewLeftUp = (View) findViewById(R.id.leftUp);
		mViewLeftDown = (View) findViewById(R.id.leftDown);
		mViewRightUp = (View) findViewById(R.id.rightUp);
		mViewRightMiddle = (View) findViewById(R.id.rightMiddle);
		mViewRightDown = (View) findViewById(R.id.rightDown);

		mSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				final float[] hsvColor1 = {0, 1, 1};
				final float[] hsvColor2 = {0, 1, 1};
				final float[] hsvColor3 = {0, 1, 1};
				final float[] hsvColor4 = {0, 1, 1};
				final float[] hsvColor5 = {0, 1, 1};
				// generate only hue component in range [0, 360),
				// leaving saturation and brightness maximum possible
				hsvColor1[0] = 200f * progress / 100;
				hsvColor2[0] = 300f * progress / 100;
				hsvColor3[0] = 100f * progress / 100;
				hsvColor4[0] = 170f * progress / 100;
				hsvColor5[0] = 230 * progress / 100;
				
				mViewLeftUp.setBackgroundColor(Color.HSVToColor(hsvColor1));
				mViewLeftDown.setBackgroundColor(Color.HSVToColor(hsvColor2));
				mViewRightUp.setBackgroundColor(Color.HSVToColor(hsvColor3));
				mViewRightMiddle.setBackgroundColor(Color.HSVToColor(hsvColor4));
				mViewRightDown.setBackgroundColor(Color.HSVToColor(hsvColor5));
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_MORE_INFO, Menu.NONE, "More information");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MORE_INFO:
			
			createDialog(INFORMATION_TAG);
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void createDialog(int dialogID) {

		switch(dialogID){
		
		case INFORMATION_TAG:
			
			mFragmentDialog =  AlertDialogFragment.newInstance();
			mFragmentDialog.show(getFragmentManager(), DIALOG_INFO_WEBSITE);
			
			break;
			
		default :
			break;
		
		}
		
	}
	
	
	public static class AlertDialogFragment extends DialogFragment{
		


		public static AlertDialogFragment newInstance(){
			return new AlertDialogFragment();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			return new AlertDialog.Builder(getActivity())
				.setMessage(DIALOG_INFO_WEBSITE)
				.setCancelable(true)
				.setPositiveButton("Visit MOMA", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MOMA_WEBSITE));
								startActivity(mIntent);
								
							}
						})
				.setNegativeButton("Not now",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							//Nothing
							
						}
					})
				.create();
		}
		
		
		
	}
}
