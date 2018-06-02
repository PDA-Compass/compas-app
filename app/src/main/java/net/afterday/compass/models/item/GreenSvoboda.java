package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationSumModifier;

public class GreenSvoboda extends InventoryItem
    implements BoosterModifier, RadiationSumModifier
{
    public int getName() {
        return R.string.item_green_svoboda;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_green_svoboda_serum;
    }

    public long getDuration() {
        return 30L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return 0.1d;
    }

    public GreenSvoboda() {
        super();
    }

    public GreenSvoboda(Parcel in) {
        super(in);
    }

    public static final Creator<GreenSvoboda> CREATOR =
            new Creator<GreenSvoboda>() {
                @Override
                public GreenSvoboda createFromParcel(Parcel source) {
                    return new GreenSvoboda(source);
                }

                @Override
                public GreenSvoboda[] newArray(int size) {
                    return new GreenSvoboda[size];
                }
            };
}
