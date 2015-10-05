package com.polant.projectsport.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Time;
import java.util.Date;

/**
 * Created by ����� on 05.10.2015.
 */
public class MyContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.polant.projectsport.data/information");

    public static final int ALL_ROWS = 1;
    public static final int SINGLE_ROW = 2;

    //--------------------------//
    //��� ������� ���� ������.

    //USER
    public static final String ID_USER = "ID_USER";
    public static final String USER_WEIGHT = "USER_WEIGHT";
    public static final String USER_HEIGHT = "USER_HEIGHT";
    public static final String USER_SEX = "USER_SEX";

    //FOOD
    public static final String ID_FOOD = "_id";
    public static final String FOOD_CATEGORY = "FOOD_CATEGORY";

    //SPECIFIC_FOOD
    public static final String ID_SPECIFIC_FOOD = "_id";
    public static final String FOOD_NAME = "FOOD_NAME";
    //+ � ������� ���� FOOD_CATEGORY ��� ������� ����.
    public static final String CAL_COUNT = "CAL_COUNT";

    //STATISTICS
    public static final String ID_STATISTICS = "ID_STATISTICS";
    ////+ � ������� ���� ID_USER ��� ������� ����.
    ////+ � ������� ���� FOOD_NAME ��� ������� ����.
    public static final String DAY = "DAY";
    public static final String MONTH = "DAY";
    public static final String YEAR = "YEAR";


    //---------------------------//

    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.polant.projectsport.data", "information", ALL_ROWS);
        uriMatcher.addURI("com.polant.projectsport.data", "information/#", SINGLE_ROW);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri))
        {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/vnd.polant.information";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.polant.information";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    private static class SportOpenHelper extends SQLiteOpenHelper {

        private static final String LOG = SportOpenHelper.class.getName();

        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_NAME = "sport.db";

        //��� �������.
        private static final String TABLE_USER = "USER_TABLE";
        private static final String TABLE_STATISTICS = "STATISTICS_TABLE";
        private static final String TABLE_SPECIFIC_FOOD = "SPECIFIC_FOOD_TABLE";
        private static final String TABLE_FOOD = "FOOD_TABLE";

        private static final String CREATE_TABLE_USER = "Create table " + TABLE_USER + " (" +
                ID_USER + " integer primary key autoincrement, " +
                USER_HEIGHT + " FLOAT, " +
                USER_WEIGHT + " FLOAT, " +
                USER_SEX + " TEXT);";

        private static final String CREATE_TABLE_FOOD =
                "Create table " + TABLE_FOOD + " (" +
                        ID_FOOD + " integer primary key autoincrement, " +
                        FOOD_CATEGORY + " TEXT);";

        private static final String CREATE_TABLE_SPECIFIC_FOOD =
                "Create table " + TABLE_SPECIFIC_FOOD + " (" +
                        ID_SPECIFIC_FOOD + " integer primary key autoincrement, " +
                        FOOD_NAME + " TEXT, " +
                        FOOD_CATEGORY + " TEXT " +
                        CAL_COUNT + " INTEGER);";

        private static final String CREATE_TABLE_STATISTICS =
                "Create table " + TABLE_STATISTICS + " (" +
                        ID_STATISTICS + " integer primary key autoincrement, " +
                        ID_USER + " INTEGER, " +
                        FOOD_NAME + " TEXT, " +
                        FOOD_CATEGORY + " TEXT, " +
                        DAY + " TEXT, " +
                        MONTH + " TEXT, " +
                        YEAR + " TEXT);";

        //�����������.
        public SportOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG, "----- Create database  -----");

            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_FOOD);
            db.execSQL(CREATE_TABLE_SPECIFIC_FOOD);
            db.execSQL(CREATE_TABLE_STATISTICS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOG, "Update database from " + oldVersion + " to " + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER + ";");
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_FOOD + ";");
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SPECIFIC_FOOD + ";");
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STATISTICS + ";");

            onCreate(db);
        }
    }


}