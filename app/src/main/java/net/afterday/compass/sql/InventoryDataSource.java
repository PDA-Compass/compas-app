package net.afterday.compass.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import net.afterday.compass.models.InventoryItem;

import java.util.ArrayList;

public class InventoryDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_ITEM,
            SQLiteHelper.COLUMN_DATE
    };

    public InventoryDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long create(InventoryItem item) {

        String text = item.getClass().getName();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_ITEM, text);

        return database.insert(SQLiteHelper.TABLE_INVENTORY, null, values);
    }

    public int remove(long id) {
        return database.delete(SQLiteHelper.TABLE_INVENTORY, SQLiteHelper.COLUMN_ID + "=" + id, null);
    }

    public ArrayList<InventoryItem> getList() {
        ArrayList<InventoryItem> list = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_INVENTORY, allColumns, null, null, null, null, SQLiteHelper.COLUMN_DATE + " DESC", null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            InventoryItem item = cursorToResult(cursor);
            if (item != null) {
                list.add(item);
            }
            cursor.moveToNext();
        }

        cursor.close();

        return list;
    }

    private InventoryItem cursorToResult(Cursor cursor) {
        InventoryItem item;
        try {
            item = (InventoryItem) Class.forName(cursor.getString(1)).newInstance();
            item.setId(cursor.getLong(0));

            return item;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
