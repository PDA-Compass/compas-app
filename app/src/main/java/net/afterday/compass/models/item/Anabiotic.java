package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;

public class Anabiotic extends InventoryItem
        implements HealthInstant
{
    public int getName() {
        return R.string.item_anabiotic;
    }

    public String getDescription() {
        return "Simple medkit";
    }

    public int getImage() {
        return R.drawable.item_anabiotic;
    }

    @Override
    public double getInstantHealth() {
        return -30d;
    }

    public Anabiotic() {
        super();
    }

    public Anabiotic(Parcel in) {
        super(in);
    }

    public static final Creator<Anabiotic> CREATOR =
            new Creator<Anabiotic>() {
                @Override
                public Anabiotic createFromParcel(Parcel source) {
                    return new Anabiotic(source);
                }

                @Override
                public Anabiotic[] newArray(int size) {
                    return new Anabiotic[size];
                }
            };
}
