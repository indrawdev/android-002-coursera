package com.henry2man.dailyselfie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DataHelper extends SQLiteOpenHelper {

	public  static final String DATABASE_NAME = "SELFIES";
	public  static final int DATABASE_VERSION = 1;

	public static final String SELFIES_TABLE_NAME = "selfies";
	public static final String SELFIES_TABLE_ID = BaseColumns._ID;
	public static final String SELFIES_TABLE_PATH = "PATH";
	public static final String SELFIES_TABLE_POSITION = "POSITION";
	public static final String SELFIES_TABLE_DATE_TAKEN = "DATE_TAKEN";

	private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE "
			+ SELFIES_TABLE_NAME + " (" + SELFIES_TABLE_ID
			+ " INTEGER PRIMARY KEY, " + SELFIES_TABLE_PATH + " TEXT, "
			+ SELFIES_TABLE_POSITION + " INTEGER UNIQUE, "
			+ SELFIES_TABLE_DATE_TAKEN + " LONG );";

	public DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// DO NOTHING
	}
}
