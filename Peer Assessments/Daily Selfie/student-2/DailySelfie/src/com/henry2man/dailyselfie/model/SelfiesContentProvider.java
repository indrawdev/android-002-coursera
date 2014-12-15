package com.henry2man.dailyselfie.model;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.henry2man.dailyselfie.DataHelper;

public class SelfiesContentProvider extends ContentProvider {

	private static final String COUNT_ALL = "count(*)";

	public static final String[] ALL_COLLUMNS = { DataHelper.SELFIES_TABLE_ID,
			DataHelper.SELFIES_TABLE_PATH, DataHelper.SELFIES_TABLE_POSITION,
			DataHelper.SELFIES_TABLE_DATE_TAKEN };

	public static final int SELFIES = 10;
	public static final int SELFIE_ID = 20;

	private static final String AUTHORITY = "com.henry2man.dailyselfie.contentprovider";

	private static final String BASE_PATH = "selfies";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/selfies";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/selfie";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, SELFIES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SELFIE_ID);
	}

	// Database fields

	private DataHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new DataHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(DataHelper.SELFIES_TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case SELFIES:
			break;
		case SELFIE_ID:
			// adding the ID to the original query
			queryBuilder.appendWhere(DataHelper.SELFIES_TABLE_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case SELFIES:
			id = sqlDB.insert(DataHelper.SELFIES_TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(CONTENT_URI + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case SELFIES:
			rowsDeleted = sqlDB.delete(DataHelper.SELFIES_TABLE_NAME,
					selection, selectionArgs);
			break;
		case SELFIE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(DataHelper.SELFIES_TABLE_NAME,
						DataHelper.SELFIES_TABLE_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(DataHelper.SELFIES_TABLE_NAME,
						DataHelper.SELFIES_TABLE_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case SELFIES:
			rowsUpdated = sqlDB.update(DataHelper.SELFIES_TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case SELFIE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DataHelper.SELFIES_TABLE_NAME,
						values, DataHelper.SELFIES_TABLE_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(DataHelper.SELFIES_TABLE_NAME,
						values, DataHelper.SELFIES_TABLE_ID + "=" + id
								+ " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {

		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(ALL_COLLUMNS));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				// Special case, _COUNT column
				if (!(requestedColumns.contains(COUNT_ALL) && requestedColumns
						.size() == 1)) {
					throw new IllegalArgumentException(
							"Unknown columns in projection");
				}
			}
		}
	}

}
