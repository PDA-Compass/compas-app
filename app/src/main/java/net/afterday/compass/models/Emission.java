package net.afterday.compass.models;

import java.util.Calendar;
import java.util.TimeZone;

public class Emission
{
    private Calendar mWarn;
    private Calendar mStart;
    private Calendar mEnd;
    private int mType;

    public Emission(
            int warnDay, int warnHour, int warnMinute,
            int startDay, int startHour, int startMinute,
            int endDay, int endHour, int endMinute,
            int type
    ) {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");

        mType = type;

        // Set start date
        mWarn = Calendar.getInstance();
        mStart = Calendar.getInstance();
        mEnd = Calendar.getInstance();

        mWarn.setTimeZone(tz);
        mStart.setTimeZone(tz);
        mEnd.setTimeZone(tz);

        mWarn.set(2018, 5, warnDay, warnHour, warnMinute);
        mStart.set(2018, 5, startDay, startHour, startMinute);
        mEnd.set(2018, 5, endDay, endHour, endMinute);
    }

    public boolean warning(Calendar calendar) {
        return calendar.after(mWarn) && calendar.before(mStart);
    }

    public boolean active(Calendar calendar) {
        return calendar.after(mStart) && calendar.before(mEnd);
    }

    public int getType() {
        return mType;
    }

    public void deactivate() {
        mWarn.set(2018, 5, 23, 16, 53);
        mStart.set(2018, 5, 23, 16, 55);
        mEnd.set(2018, 5, 23, 16, 59);
    }
}
