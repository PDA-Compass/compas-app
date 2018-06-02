package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.HealthModifier;
import net.afterday.compass.models.InventoryItem;

public class Hercules extends InventoryItem
    implements BoosterModifier, HealthModifier
{
    public int getName() {
        return R.string.item_hercules;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_hercules;
    }

    public long getDuration() {
        return 30L * 60L * 1000L;
    }

    public double getHealthModifier() {
        return 1.5d;
    }

    public Hercules() {
        super();
    }

    public Hercules(Parcel in) {
        super(in);
    }

    public static final Creator<Hercules> CREATOR =
            new Creator<Hercules>() {
                @Override
                public Hercules createFromParcel(Parcel source) {
                    return new Hercules(source);
                }

                @Override
                public Hercules[] newArray(int size) {
                    return new Hercules[size];
                }
            };
}
