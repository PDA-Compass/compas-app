package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.ArmorModifier;
import net.afterday.compass.models.HealthModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationSumModifier;

public class Exoskeleton extends InventoryItem
    implements ArmorModifier, AnomalyModifier, RadiationSumModifier, PsiModifier, HealthModifier
{
    public int getName() {
        return R.string.item_exoskeleton;
    }

    public String getDescription() {
        return "Simple leather jacket. Makes you more resistant to collected radiation";
    }

    public int getImage() {
        return R.drawable.item_exoskeleton;
    }

    public long getDuration() {
        return 10L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return .7d;
    }

    public double getAnomalyModifier() {
        return 0.6d;
    }

    public double getPsiModifier() {
        return 0.9d;
    }

    public double getHealthModifier() {
        return 2d;
    }

    public Exoskeleton() {
        super();
    }

    public Exoskeleton(Parcel in) {
        super(in);
    }

    public static final Creator<Exoskeleton> CREATOR =
            new Creator<Exoskeleton>() {
                @Override
                public Exoskeleton createFromParcel(Parcel source) {
                    return new Exoskeleton(source);
                }

                @Override
                public Exoskeleton[] newArray(int size) {
                    return new Exoskeleton[size];
                }
            };
}
