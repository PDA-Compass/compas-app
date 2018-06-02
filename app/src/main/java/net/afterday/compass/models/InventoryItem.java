package net.afterday.compass.models;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class InventoryItem implements Parcelable
{
    private long mActivated;
    private long mId;

    public abstract int getName();
    public abstract String getDescription();
    public abstract int getImage();

    public InventoryItem() {}

    public InventoryItem(Parcel in) {
        mActivated = in.readLong();
        mId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mActivated);
        dest.writeLong(mId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public InventoryItem setId(long id) {
        mId = id;
        return this;
    }

    public long getId() {
        return mId;
    }

    public InventoryItem activate() {
        mActivated = System.currentTimeMillis();
        return this;
    }

    public InventoryItem setActivated(long time) {
        mActivated = time;
        return this;
    }

    public long getActivated() {
        return mActivated;
    }
}
