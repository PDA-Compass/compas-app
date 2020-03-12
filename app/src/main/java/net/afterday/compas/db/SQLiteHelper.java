package net.afterday.compas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{
    public static final String TABLE_LOG = "messages";
    public static final String TABLE_INVENTORY = "inventory";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "created";
    public static final String COLUMN_DATE_DISPLAY = "date_display";
    public static final String COLUMN_TEXT = "content";
    public static final String COLUMN_TYPE = "type";

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_LOG = "create table "
            + TABLE_LOG + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " datetime default CURRENT_TIMESTAMP, "
            + COLUMN_DATE_DISPLAY + " text not null, "
            + COLUMN_TEXT + " text not null, "
            + COLUMN_TYPE + " integer default 0"
            + ");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        onCreate(db);
    }
}
