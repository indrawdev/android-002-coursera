package com.henry2man.dailyselfie;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.henry2man.dailyselfie.io.FileUtils;
import com.henry2man.dailyselfie.model.Selfie;

public class ViewSelfieActivity extends Activity {

	public static final String SELFIE_ID = "ViewSelfieActivity.SELFIE_ID";

	private SelfiesDataSource mDatasource;
	private long mSelfieId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_selfie);
		if (savedInstanceState == null) {

			mSelfieId = getIntent().getExtras().getLong(SELFIE_ID);

			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		mDatasource = new SelfiesDataSource(this);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view_selfie,
					container, false);

			TextView selfieTitle = (TextView) rootView
					.findViewById(R.id.selfie_title);

			TextView selfiePath = (TextView) rootView
					.findViewById(R.id.selfie_path);
			
			ImageView imageView = (ImageView) rootView
					.findViewById(R.id.selfie_view);

			Selfie selfie = mDatasource.getSelfieById(mSelfieId);

			if (FileUtils.selfieExists(selfie)) {
				selfieTitle.setText(container.getContext().getString(
						R.string.selfie_taked_on)
						+ ": " + selfie.getDateTaken());
			} else {
				// Show error message!
				selfieTitle.setText(container.getContext().getString(
						R.string.selfie_maybe_deleted));
			}

			imageView.setImageBitmap(ImageUtils.computeImageBitmap(imageView,
					selfie));

			selfiePath.setText(container.getContext().getString(
					R.string.selfie_path)
					+ ": " + selfie.getPath());

			return rootView;
		}
	}
}
