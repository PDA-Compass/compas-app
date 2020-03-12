package net.afterday.compas.db.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.afterday.compas.db.SQLiteHelper;
import net.afterday.compas.logging.LogLine;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by spaka on 6/12/2018.
 */

public class LogDb
{
    private SQLiteHelper dbHelper;

    private String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_DATE_DISPLAY,
            SQLiteHelper.COLUMN_TEXT,
            SQLiteHelper.COLUMN_TYPE
    };

    public LogDb(SQLiteHelper dbHelper)
    {
        this.dbHelper = dbHelper;
    }

    public List<LogLine> getLogLines()
    {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<LogLine> list = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_LOG, allColumns, null, null, null, null, SQLiteHelper.COLUMN_DATE + " ASC", "100");

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            list.add(cursorToResult(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    public void putLogLine(LogLine line)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_TEXT, line.getText());
        values.put(SQLiteHelper.COLUMN_TYPE, line.getColor());
        values.put(SQLiteHelper.COLUMN_DATE_DISPLAY, line.getDate());
        long id = database.insert(SQLiteHelper.TABLE_LOG, null, values);
    }

    private LogLine cursorToResult(Cursor cursor) {
        LogLine logLine = new LogLine();
        logLine
                .setId(cursor.getLong(0))
                .setDate(cursor.getString(1))
                .setText(cursor.getString(2))
                .setColor(cursor.getInt(3));

        return logLine;
    }
}
