package net.afterday.compass.sql;

import android.os.Parcel;
import android.os.Parcelable;

public class LogLine implements Parcelable {
    private long mId;
    private String mDate;
    private String mText;
    private int mType;

    public LogLine() {}
    public LogLine(Parcel in) {
        mId = in.readLong();
        mDate = in.readString();
        mText = in.readString();
        mType = in.readInt();
    }

    // Id
    public long getId() {
        return mId;
    }

    public LogLine setId(long id) {
        mId = id;

        return this;
    }

    // Date
    public String getDate() {
        return mDate;
    }

    public LogLine setDate(String date) {
        mDate = date;

        return this;
    }

    // Text
    public String getText() {
        return mText;
    }

    public LogLine setText(String text) {
        mText = text;

        return this;
    }

    // Type
    public int getType() {
        return mType;
    }

    public LogLine setType(int type) {
        mType = type;

        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mDate);
        dest.writeString(mText);
        dest.writeInt(mType);
    }

    public static final Parcelable.Creator<LogLine> CREATOR =
            new Parcelable.Creator<LogLine>() {
                @Override
                public LogLine createFromParcel(Parcel source) {
                    return new LogLine(source);
                }

                @Override
                public LogLine[] newArray(int size) {
                    return new LogLine[size];
                }
            };

    @Override
    public String toString() {
        return String.format("%1s: %2s", mDate, mText);
    }
}
