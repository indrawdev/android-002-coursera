package com.coursera.assignment.week8.selfie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by viswaviv on 28/11/2014.
 */
public class SelfiDatabaseHelper extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "SelfiImagesTable";

    public static final String ID_COL_NAME = "_id";
    public static final String IMAGE_URL_COL_NAME = "Image";
    public static final String IMAGE_DESCRIPTION_COLNAME = "ImageDescription";

    final static String[] COLUMNS = { ID_COL_NAME,IMAGE_URL_COL_NAME, IMAGE_DESCRIPTION_COLNAME };
    final private static String CREATE_CMD =

            "CREATE TABLE " + TABLE_NAME + " ("
                    + ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMAGE_URL_COL_NAME+ " TEXT NOT NULL, "
                    + IMAGE_DESCRIPTION_COLNAME + " TEXT NOT NULL)";

    final private static String DB_NAME = "selfi_db";
    final private static Integer VERSION = 1;
    final private Context mContext;
    public SelfiDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}