package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.ArmorModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationSumModifier;

public class Seva extends InventoryItem
    implements ArmorModifier, AnomalyModifier, RadiationSumModifier, PsiModifier
{
    public int getName() {
        return R.string.item_seva_suit;
    }

    public String getDescription() {
        return "Simple leather jacket. Makes you more resistant to collected radiation";
    }

    public int getImage() {
        return R.drawable.item_seva_suit;
    }

    public long getDuration() {
        return 10L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return .6d;
    }

    public double getAnomalyModifier() {
        return 0.7d;
    }

    public double getPsiModifier() {
        return 0.8d;
    }

    public Seva() {
        super();
    }

    public Seva(Parcel in) {
        super(in);
    }

    public static final Creator<Seva> CREATOR =
            new Creator<Seva>() {
                @Override
                public Seva createFromParcel(Parcel source) {
                    return new Seva(source);
                }

                @Override
                public Seva[] newArray(int size) {
                    return new Seva[size];
                }
            };
}
