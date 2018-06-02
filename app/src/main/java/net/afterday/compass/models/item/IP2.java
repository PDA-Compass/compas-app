package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.InventoryItem;

public class IP2 extends InventoryItem
    implements BoosterModifier, AnomalyModifier
{
    public int getName() {
        return R.string.item_ip2;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_ip2_antidote;
    }

    public long getDuration() {
        return 60L * 1000L;
    }

    public double getAnomalyModifier() {
        return 0d;
    }

    public IP2() {
        super();
    }

    public IP2(Parcel in) {
        super(in);
    }

    public static final Creator<IP2> CREATOR =
            new Creator<IP2>() {
                @Override
                public IP2 createFromParcel(Parcel source) {
                    return new IP2(source);
                }

                @Override
                public IP2[] newArray(int size) {
                    return new IP2[size];
                }
            };
}
