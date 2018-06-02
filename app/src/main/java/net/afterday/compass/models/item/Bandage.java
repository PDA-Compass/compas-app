package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;

public class Bandage extends InventoryItem
    implements HealthInstant
{
    public int getName() {
        return R.string.item_bandage;
    }

    public String getDescription() {
        return "Bandage your wounds";
    }

    public int getImage() {
        return R.drawable.item_bandage;
    }

    @Override
    public double getInstantHealth() {
        return 10d;
    }

    public Bandage() {
        super();
    }

    public Bandage(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Bandage> CREATOR =
            new Parcelable.Creator<Bandage>() {
                @Override
                public Bandage createFromParcel(Parcel source) {
                    return new Bandage(source);
                }

                @Override
                public Bandage[] newArray(int size) {
                    return new Bandage[size];
                }
            };
}
