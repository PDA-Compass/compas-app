package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.ArmorModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationSumModifier;

public class LeatherJacket extends InventoryItem
    implements ArmorModifier, RadiationSumModifier
{
    public int getName() {
        return R.string.item_leather_jacket;
    }

    public String getDescription() {
        return "Simple leather jacket. Makes you more resistant to collected radiation";
    }

    public int getImage() {
        return R.drawable.item_leather_jacket;
    }

    public long getDuration() {
        return 10L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return .9d;
    }

    public LeatherJacket() {
        super();
    }

    public LeatherJacket(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<LeatherJacket> CREATOR =
            new Parcelable.Creator<LeatherJacket>() {
                @Override
                public LeatherJacket createFromParcel(Parcel source) {
                    return new LeatherJacket(source);
                }

                @Override
                public LeatherJacket[] newArray(int size) {
                    return new LeatherJacket[size];
                }
            };
}
