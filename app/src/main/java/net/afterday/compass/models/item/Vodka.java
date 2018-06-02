package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationInstant;

public class Vodka extends InventoryItem
        implements HealthInstant, RadiationInstant
{
    public int getName() {
        return R.string.item_vodka;
    }

    public String getDescription() {
        return "Simple medkit";
    }

    public int getImage() {
        return R.drawable.item_vodka;
    }

    @Override
    public double getInstantHealth() {
        return -10d;
    }

    public double getRadiationInstant() {
        return -1d;
    }

    public Vodka() {
        super();
    }

    public Vodka(Parcel in) {
        super(in);
    }

    public static final Creator<Vodka> CREATOR =
            new Creator<Vodka>() {
                @Override
                public Vodka createFromParcel(Parcel source) {
                    return new Vodka(source);
                }

                @Override
                public Vodka[] newArray(int size) {
                    return new Vodka[size];
                }
            };
}
