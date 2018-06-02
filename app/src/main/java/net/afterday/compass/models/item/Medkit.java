package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.HealthInstant;

public class Medkit extends InventoryItem
        implements HealthInstant
{
    public int getName() {
        return R.string.item_medkit;
    }

    public String getDescription() {
        return "Simple medkit";
    }

    public int getImage() {
        return R.drawable.item_medkit;
    }

    @Override
    public double getInstantHealth() {
        return 20d;
    }

    public Medkit() {
        super();
    }

    public Medkit(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Medkit> CREATOR =
            new Parcelable.Creator<Medkit>() {
                @Override
                public Medkit createFromParcel(Parcel source) {
                    return new Medkit(source);
                }

                @Override
                public Medkit[] newArray(int size) {
                    return new Medkit[size];
                }
            };
}
