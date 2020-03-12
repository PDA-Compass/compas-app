package net.afterday.compas.logging;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Events.AddItem;
import net.afterday.compas.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.db.DataBase;
import net.afterday.compas.db.log.LogDb;
import net.afterday.compas.devices.vibro.Vibro;
import net.afterday.compas.persistency.items.ItemDescriptor;
import net.afterday.compas.view.SmallLogListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 3/20/2018.
 */

public class Logger
{
    private static SmallLogListAdapter logListAdapter;
    private static Subject<LogLine> logLinesSubj = BehaviorSubject.create();
    private static Subject<List<LogLine>> logLines = BehaviorSubject.create();
    private static Logger instance;
    private static LogDb logDb;
    private static Context ctx;
    private static Vibro vibro;

    private Logger(Context ctx, Vibro vibro)
    {
        Logger.ctx = ctx;
        logDb = DataBase.instance().logDb();
        Logger.vibro = vibro;
        logLinesSubj.scan(logDb.getLogLines(),
                (lst, line) -> {
                    lst.add(line);
                    if(lst.size() > 50)
                    {
                        lst = new ArrayList<>(lst.subList(2, lst.size()));
                    }
                    return lst;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    logLines.onNext(list);
                });
    }

    public static Logger instance(Context ctx, Vibro vibro)
    {
        instance = new Logger(ctx, vibro);
        return instance;
    }

    public static Logger instance()
    {
        if(instance != null)
        {
            return instance;
        }
        throw new IllegalStateException("Logger not initialized");
    }

    public static void i(String message)
    {
        String time = getTime();
        log(new LogLine(formMsg(time, message), time, ContextCompat.getColor(ctx, R.color.ok)), true);
    }

    public static void d(String message)
    {
        String time = getTime();
        android.util.Log.d("LOGGER:", message);
        log(new LogLine(formMsg(time, message), time, ContextCompat.getColor(ctx, R.color.norm)), true);
    }

    public static void d(int id)
    {
        String time = getTime();
        log(new LogLine(formMsg(time, ctx.getResources().getString(id)), time, ContextCompat.getColor(ctx, R.color.norm)), true);
    }

    public static void e(String message)
    {
        String time = getTime();
        log(new LogLine(formMsg(time, message), time, ContextCompat.getColor(ctx, R.color.bad)), true);
    }

    public static void e(int id)
    {
        String time = getTime();
        log(new LogLine(formMsg(time, ctx.getResources().getString(id)), time, ContextCompat.getColor(ctx, R.color.bad)), true);
    }

    public Observable<List<LogLine>> getLogStream()
    {
        return logLines;
    }

    public static void logItemAdded(ItemAdded itemAdded)
    {
        i(String.format(ctx.getString(R.string.message_received_item), getItemName(itemAdded.getItem().getItemDescriptor())));
    }

    public static void logItemUsed(Item item)
    {
        i(String.format(ctx.getString(R.string.message_used_item), getItemName(item.getItemDescriptor())));
    }

    public static void logItemDropped(Item item)
    {
        i(String.format(ctx.getString(R.string.message_dropped_item), getItemName(item.getItemDescriptor())));
    }

    private static void log(LogLine logLine, boolean addToDb)
    {
        logLinesSubj.onNext(logLine);
        //vibro.vibrateMessage();
        if(addToDb)
        {
            logDb.putLogLine(logLine);
        }
    }

    private static String getTime()
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        //int sec = c.get(Calendar.SECOND);
        return (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min);
    }

    private static String formMsg(String time, String msg)
    {
        return msg;
    }

    private static String formTime(String time, String msg)
    {
        return time;
    }

    private static String getItemName(ItemDescriptor itemD)
    {
        String itemName = ctx.getString(itemD.getNameId());
        itemName = itemName != null ? itemName : itemD.getName();
        return itemName != null ? itemName : "UNKNOWN ITEM";
    }
}
