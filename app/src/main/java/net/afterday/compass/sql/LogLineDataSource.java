package net.afterday.compass.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class LogLineDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private Context mContext;

    private String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_DATE_DISPLAY,
            SQLiteHelper.COLUMN_TEXT,
            SQLiteHelper.COLUMN_TYPE
    };

    public LogLineDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public long create(String text) {
        return create(text, 0);
    }

    public long create(String text, int type) {

        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        Calendar c = Calendar.getInstance(tz);

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_TEXT, text);
        values.put(SQLiteHelper.COLUMN_TYPE, type);
        values.put(SQLiteHelper.COLUMN_DATE_DISPLAY, String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d", c.get(Calendar.MINUTE)));

        long id = database.insert(SQLiteHelper.TABLE_LOG, null, values);

        Intent notify = new Intent("StalkerLogMessage");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(notify);

        return id;
    }

    public ArrayList<LogLine> getList() {
        ArrayList<LogLine> list = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_LOG, allColumns, null, null, null, null, SQLiteHelper.COLUMN_DATE + " DESC", "100");

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            LogLine logLine = cursorToResult(cursor);
            list.add(logLine);
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    private LogLine cursorToResult(Cursor cursor) {
        LogLine logLine = new LogLine();
        logLine
                .setId(cursor.getLong(0))
                .setDate(cursor.getString(1))
                .setText(cursor.getString(2))
                .setType(cursor.getInt(3))
        ;

        return logLine;
    }

}
