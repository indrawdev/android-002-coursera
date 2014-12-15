package com.henry2man.dailyselfie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.henry2man.dailyselfie.model.Selfie;
import com.henry2man.dailyselfie.model.SelfiesContentProvider;

public class SelfiesDataSource {

	private Context mContext;
	private ContentResolver mContentResolver;

	public SelfiesDataSource(Context mContext) {
		this.mContext = mContext;
		this.mContentResolver = mContext.getContentResolver();
	}

	public Selfie createSelfie(Uri pFileUri, int position) {
		ContentValues values = new ContentValues();
		values.put(DataHelper.SELFIES_TABLE_PATH, uriToString(pFileUri));
		values.put(DataHelper.SELFIES_TABLE_POSITION, position);
		values.put(DataHelper.SELFIES_TABLE_DATE_TAKEN, new Date().getTime());

		Uri result = mContentResolver.insert(
				SelfiesContentProvider.CONTENT_URI, values);
		Cursor cursor = mContentResolver.query( result,
				SelfiesContentProvider.ALL_COLLUMNS, null, null, null);
		cursor.moveToFirst();
		Selfie newSelfie = cursorToSelfie(cursor);
		cursor.close();
		return newSelfie;
	}

	public List<Selfie> getAllSelfies() {
		List<Selfie> selfies = new ArrayList<Selfie>();

		Cursor cursor = mContentResolver.query(
				SelfiesContentProvider.CONTENT_URI,
				SelfiesContentProvider.ALL_COLLUMNS, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Selfie selfie = cursorToSelfie(cursor);
			selfies.add(selfie);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return selfies;
	}

	private Selfie cursorToSelfie(Cursor cursor) {
		Selfie selfie = new Selfie();

		selfie.setId(cursor.getLong(0));
		selfie.setPath(cursor.getString(1));
		selfie.setPosition(cursor.getInt(2));
		selfie.setDateTaken(new Date(cursor.getLong(3)));
		return selfie;
	}

	public int getCount() {
		Cursor cursor = mContentResolver.query(
				SelfiesContentProvider.CONTENT_URI,
				new String[] {"count(*)"}, null, null, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	public Selfie getSelfieById(long mSelfieId) {
		Uri uri = Uri.parse(SelfiesContentProvider.CONTENT_URI + "/"
				+ mSelfieId);
		Cursor cursor = mContentResolver.query(uri,
				SelfiesContentProvider.ALL_COLLUMNS, null, null, null);
		cursor.moveToFirst();
		Selfie newSelfie = cursorToSelfie(cursor);
		cursor.close();
		return newSelfie;
	}

	public Selfie getSelfieByPosition(int position) {
		Cursor cursor = mContentResolver.query(
				SelfiesContentProvider.CONTENT_URI,
				SelfiesContentProvider.ALL_COLLUMNS,
				DataHelper.SELFIES_TABLE_POSITION + " = ?", new String[] { ""
						+ position }, null);
		cursor.moveToFirst();
		Selfie newSelfie = cursorToSelfie(cursor);
		cursor.close();
		return newSelfie;
	}

	private static String uriToString(Uri pFileUri) {
		return pFileUri.getPath();
	}
}
