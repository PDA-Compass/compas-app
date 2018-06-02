package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.HealthModifier;
import net.afterday.compass.models.InventoryItem;

public class EnergyDrink extends InventoryItem
    implements BoosterModifier, HealthModifier
{
    public int getName() {
        return R.string.item_energy_drink;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_energy_drink;
    }

    public long getDuration() {
        return 30L * 60L * 1000L;
    }

    public double getHealthModifier() {
        return 2d;
    }

    public EnergyDrink() {
        super();
    }

    public EnergyDrink(Parcel in) {
        super(in);
    }

    public static final Creator<EnergyDrink> CREATOR =
            new Creator<EnergyDrink>() {
                @Override
                public EnergyDrink createFromParcel(Parcel source) {
                    return new EnergyDrink(source);
                }

                @Override
                public EnergyDrink[] newArray(int size) {
                    return new EnergyDrink[size];
                }
            };
}
