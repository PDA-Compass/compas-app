package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.PsiModifier;

public class YellowSvoboda extends InventoryItem
    implements BoosterModifier, PsiModifier
{
    public int getName() {
        return R.string.item_yellow_svoboda;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_yellow_svoboda_serum;
    }

    public long getDuration() {
        return 30L * 60L * 1000L;
    }

    public double getPsiModifier() {
        return 0.1d;
    }

    public YellowSvoboda() {
        super();
    }

    public YellowSvoboda(Parcel in) {
        super(in);
    }

    public static final Creator<YellowSvoboda> CREATOR =
            new Creator<YellowSvoboda>() {
                @Override
                public YellowSvoboda createFromParcel(Parcel source) {
                    return new YellowSvoboda(source);
                }

                @Override
                public YellowSvoboda[] newArray(int size) {
                    return new YellowSvoboda[size];
                }
            };
}
