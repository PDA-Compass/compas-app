package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.ArmorModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationSumModifier;

public class MercSuit extends InventoryItem
    implements ArmorModifier, AnomalyModifier, RadiationSumModifier
{
    public int getName() {
        return R.string.item_merc_suit;
    }

    public String getDescription() {
        return "Simple leather jacket. Makes you more resistant to collected radiation";
    }

    public int getImage() {
        return R.drawable.item_merc_suit;
    }

    public long getDuration() {
        return 10L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return .8d;
    }

    public double getAnomalyModifier() {
        return 0.9d;
    }

    public MercSuit() {
        super();
    }

    public MercSuit(Parcel in) {
        super(in);
    }

    public static final Creator<MercSuit> CREATOR =
            new Creator<MercSuit>() {
                @Override
                public MercSuit createFromParcel(Parcel source) {
                    return new MercSuit(source);
                }

                @Override
                public MercSuit[] newArray(int size) {
                    return new MercSuit[size];
                }
            };
}
