package net.afterday.compas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.afterday.compas.db.log.LogDb;

/**
 * Created by spaka on 6/12/2018.
 */

public class DataBase
{
    private static DataBase instance;
    private Context ctx;
    private SQLiteHelper dbHelper;

    private DataBase(Context ctx)
    {
        this.ctx = ctx;
        dbHelper = new SQLiteHelper(ctx);
    }

    public static DataBase instance()
    {
        if(instance == null)
        {
            throw new IllegalStateException("Database not initialized");
        }
        return instance;
    }

    public static DataBase instance(Context ctx)
    {
        instance = new DataBase(ctx);
        return instance;
    }

    public LogDb logDb()
    {
        return new LogDb(dbHelper);
    }
}
