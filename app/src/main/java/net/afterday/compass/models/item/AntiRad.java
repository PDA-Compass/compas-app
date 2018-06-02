package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationInstant;

public class AntiRad extends InventoryItem
        implements HealthInstant, RadiationInstant
{
    public int getName() {
        return R.string.item_antirad;
    }

    public String getDescription() {
        return "Simple medkit";
    }

    public int getImage() {
        return R.drawable.item_antirad;
    }

    @Override
    public double getInstantHealth() {
        return -20d;
    }

    public double getRadiationInstant() {
        return -7d;
    }

    public AntiRad() {
        super();
    }

    public AntiRad(Parcel in) {
        super(in);
    }

    public static final Creator<AntiRad> CREATOR =
            new Creator<AntiRad>() {
                @Override
                public AntiRad createFromParcel(Parcel source) {
                    return new AntiRad(source);
                }

                @Override
                public AntiRad[] newArray(int size) {
                    return new AntiRad[size];
                }
            };
}
