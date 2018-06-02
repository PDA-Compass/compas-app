package net.afterday.compass.logging;

/**
 * Created by Justas Spakauskas on 3/20/2018.
 */

public class LogLine
{
    private String text,
                   date;
    private int type,
                resId = -1;

    public LogLine(String text, String date, int type)
    {
        this(text, date, type, -1);
    }

    public LogLine(String text, String date, int type, int resId)
    {
        this.text = text;
        this.date = date;
        this.type = type;
        this.resId = resId;
    }

    public String getText()
    {
        return text;
    }

    public String getDate()
    {
        return date;
    }

    public int getType()
    {
        return type;
    }

    public int getResId()
    {
        return resId;
    }

    public boolean hasResId()
    {
        return resId > -1;
    }

}
