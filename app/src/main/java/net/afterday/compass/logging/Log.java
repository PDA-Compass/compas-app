package net.afterday.compass.logging;

import net.afterday.compass.view.SmallLogListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 3/20/2018.
 */

public class Log
{
    //TODO: panaikinti sita spageti. Reikia naudoti abstrakcija!
    private static SmallLogListAdapter logListAdapter;
    private static List<LogLine> logLines = new ArrayList<>();
    private static Subject<LogLine> logLinesSubj = BehaviorSubject.create();

    static {
        logLinesSubj.scan(new ArrayList<LogLine>(),
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
                    if(logListAdapter != null)
                    {
                        logListAdapter.setDataset(list);
                        logListAdapter.notifyDataSetChanged();
                        //android.util.Log.wtf("Log", "NOTIFIED: " + list + logListAdapter);
                    }
                });
    }

    public static void d(String message)
    {
        String time = getTime();
        android.util.Log.d("LOGGER:", message);
        logLinesSubj.onNext(new LogLine(formMsg(time, message), time, 0));
    }

    public static void d(int id)
    {
        String time = getTime();
        logLinesSubj.onNext(new LogLine(formMsg(time, null), time, 0, id));
    }

    public static void e(String message)
    {
        String time = getTime();
        logLinesSubj.onNext(new LogLine(formMsg(time, message), time, 1));
    }

    public static void registerLogListAdapter(SmallLogListAdapter logListAdapter)
    {
        Log.logListAdapter = logListAdapter;
    }

    public static void unregisterLogListAdapter()
    {
        Log.logListAdapter = null;
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
        return " " + msg;
    }

    private static String formTime(String time, String msg)
    {
        return time;
    }
}
