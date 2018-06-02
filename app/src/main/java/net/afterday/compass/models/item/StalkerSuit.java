package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.ArmorModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationSumModifier;

public class StalkerSuit extends InventoryItem
    implements ArmorModifier, AnomalyModifier, RadiationSumModifier, PsiModifier
{
    public int getName() {
        return R.string.item_stalker_suit;
    }

    public String getDescription() {
        return "Simple leather jacket. Makes you more resistant to collected radiation";
    }

    public int getImage() {
        return R.drawable.item_stalker_suit;
    }

    public long getDuration() {
        return 10L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return .7d;
    }

    public double getAnomalyModifier() {
        return 0.8d;
    }

    public double getPsiModifier() {
        return 0.9d;
    }

    public StalkerSuit() {
        super();
    }

    public StalkerSuit(Parcel in) {
        super(in);
    }

    public static final Creator<StalkerSuit> CREATOR =
            new Creator<StalkerSuit>() {
                @Override
                public StalkerSuit createFromParcel(Parcel source) {
                    return new StalkerSuit(source);
                }

                @Override
                public StalkerSuit[] newArray(int size) {
                    return new StalkerSuit[size];
                }
            };
}
